# 스프링 데이터 JPA 페이징과 정렬

### 페이징과 정렬 파라미터 & 반환 타입 

- 페이징과 정렬 파라미터
  - `org.springframework.data.domain.Sort` : 정렬 기능
  - `org.springframework.data.domain.Pageable` : 페이징 기능 (내부에 Sort 포함)
- 특별한 반환 타입
  - `org.springframework.data.domain.Page` : 추가 count 쿼리 결과를 포함하는 페이징
  - `org.springframework.data.domain.Slice`: 추가 count 쿼리 없이 다음 페이지만 확인 가능
    - 내부적으로 limit + 1조회
  - List(자바 컬렉션): 추가 count 쿼리 없이 결과만 반환
- 분석 
  - Spring data - (JPA, Redis, Mongo) 여러가지 존재 
  - RDB든 NoSQL 기반 DB든 페이징은 필요 
  - `Spring data`는 공통화를 통해서 페이징, 정렬 제공 

### 페이징과 정렬 사용 예제

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findByUsername(String name, Pageable pageable);  //count 쿼리 사용
    Slice<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
    List<Member> findByUsername(String name, Pageable pageable);  //count 쿼리 사용 안함
    List<Member> findByUsername(String name, Sort sort);
    Page<Member> findByAge(int age, Pageable pageable);
}
```

다음 조건으로 페이징과 정렬을 사용하는 예제 코드를 보자.
- 검색 조건: 나이가 10살
- 정렬 조건: 이름으로 내림차순
- 페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 3건
- `Page<Member> findByAge(int age, Pageable pageable);`

Page 사용 예제 실행 코드
```java
@Test
public void paging(){
    // given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 10));
    memberRepository.save(new Member("member5", 10));

    int age = 10;
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

    // when
    Page<Member> page = memberRepository.findByAge(age, pageRequest);

    // then
    List<Member> content = page.getContent();       // 조회된 데이터 
    long totalElements = page.getTotalElements();   // 전체 데이터 수 

    for (Member member : content) {
        System.out.println("member = " + member);
    }
    System.out.println("totalElements = " + totalElements);

    assertThat(content.size()).isEqualTo(3);       // 조회된 데이터 수 
    assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
    assertThat(page.getNumber()).isEqualTo(0);  // 페이지 번호 
    assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호 
    assertThat(page.isFirst()).isTrue(); // 첫번째 항목인가?
    assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가? 
}
```
- 두 번째 파라미터로 받은 `Pageable`은 인터페이스다. 따라서 실제 사용할 때는 해당 인터페이스를 구현한
  `org.springframework.data.domain.PageRequest` 객체를 사용한다. 
- PageRequest 생성자의 첫 번째 파라미터에는 현재 페이지를, 두 번째 파라미터에는 조회할 데이터 수를 입력 한다.
- 여기에 추가로 정렬 정보도 파라미터로 사용할 수 있다. 참고로 페이지는 0부터 시작한다.

### count 쿼리를 분리하기 

```java
@Query(value = "select m from Member m left join m.team t", 
       countQuery = "select count(m.username) from Member m")
Page<Member> findMemberAllCountBy(Pageable pageable);
```
- 카운트 쿼리 분리 데이터는 left join, 카운트는 left join 안해도 됨 
- 전체 count 쿼리는 매우 무겁다 ➡️ 분리 

### 페이지 유지하면서 엔티티 ➡️ DTO 변환 

```java
// 엔티티 -> DTO
Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));
```

### 스프링 부트 3 - 하이버네이트 6 left join 최적화

- 스프링 부트 3 이상을 사용하면 하이버네이트 6이 적용
- 하이버네이트 6에서는 의미없는 `left join`을 최적화 해버린다
- 다음을 실행하면 `SQL`이 `LEFT JOIN`을 하지 않는 것으로 보인다.

```java
@Query(value = "select m from Member m left join m.team t")
 Page<Member> findByAge(int age, Pageable pageable);
```

실행 결과 - SQL
```sql
select
 m1_0.member_id,
 m1_0.age,
 m1_0.team_id,
 m1_0.username 
   from
   member m1_0
```

하이버네이트 6은 이런 경우 왜 `left join`을 제거하는 최적화를 할까?
- 실행한 `JPQL`을 보면 `left join`을 사용하고 있다.
- `select m from Member m left join m.team t`
- Member 와 Team 을 조인을 하지만 사실 이 쿼리를 Team 을 전혀 사용하지 않는다.
- select 절이나, where 절에서 사용하지 않는 다는 뜻이다. 그렇다면 이 `JPQL`은 사실상 다음과 같다
- `select m from Member m`
- left join 이기 때문에 왼쪽에 있는 member 자체를 다 조회한다는 뜻이 된다.
- 만약 select 나, where 에 team 의 조건이 들어간다면 정상적인 join 문이 보인다.
- `JPA`는 이 경우 최적화를 해서 join 없이 해당 내용만으로 `SQL`을 만든다.
- 여기서 만약 Member 와 Team 을 하나의 `SQL`로 한번에 조회하고 싶으시다면 `JPA`가 제공하는 
  `fetch join`을 사용해야 한다. 
- `select m from Member m left join fetch m.team t`