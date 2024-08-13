# Query By Example

- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#query-by-example

### 예제 코드1

```java
@Test
public void queryByExample(){
    //given
    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member m1 = new Member("m1", 0, teamA);
    Member m2 = new Member("m2", 0, teamA);
    em.persist(m1);
    em.persist(m2);

    em.flush();
    em.clear();

    //when
    Member member = new Member("m1"); // Probe

    ExampleMatcher matcher = ExampleMatcher.matching() // ExampleMatcher
            .withIgnorePaths("age");

    Example<Member> example = Example.of(member, matcher); // Example

    List<Member> result = memberRepository.findAll(example);

    assertThat(result.get(0).getUsername()).isEqualTo("m1");

}
```
- `Probe`: 필드에 데이터가 있는 실제 도메인 객체
- `ExampleMatcher`: 특정 필드를 일치시키는 상세한 정보 제공, 재사용 가능
- `Example`: `Probe`와 `ExampleMatcher`로 구성, 쿼리를 생성하는데 사용


### 예제 코드2

```java
@Test
public void queryByExample(){
    //given
    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member m1 = new Member("m1", 0, teamA);
    Member m2 = new Member("m2", 0, teamA);
    em.persist(m1);
    em.persist(m2);

    em.flush();
    em.clear();

    //when
    Member member = new Member("m1"); // Probe: 필드에 데이터가 있는 실제 도메인 객체
    Team team = new Team("teamA");
    member.setTeam(team);

    ExampleMatcher matcher = ExampleMatcher.matching() // ExampleMatcher: 특정 필드를 일치시키는 상세한 정보 제공
            .withIgnorePaths("age");

    Example<Member> example = Example.of(member, matcher); // Probe + ExampleMatcher 구성, 쿼리 생성하는데 사용

    List<Member> result = memberRepository.findAll(example);

    assertThat(result.get(0).getUsername()).isEqualTo("m1");
}
```
- `Probe`: Member 도메인 객체에 팀 연관관계 매핑 

실행 결과 
```sql
select
    m1_0.member_id,
    m1_0.age,
    m1_0.created_by,
    m1_0.created_date,
    m1_0.last_modified_by,
    m1_0.last_modified_date,
    m1_0.team_id,
    m1_0.username 
from
    member m1_0 
join
    team t1_0 
        on t1_0.team_id=m1_0.team_id 
where
    t1_0.name=? 
    and m1_0.username=?
```
- Inner Join(내부 조인)실행 

### 정리 

- 장점 
  - 동적 쿼리를 편리하게 처리
  - 도메인 객체를 그대로 사용
  - 데이터 저장소를 `RDB`에서 `NOSQL`로 변경해도 코드 변경이 없게 추상화 되어 있음
  - 스프링 데이터 `JPA JpaRepository` 인터페이스에 이미 포함
- 단점 
  - 조인은 가능하지만 내부 조인(INNER JOIN)만 가능함 외부 조인(LEFT JOIN) 안됨
  - 다음과 같은 중첩 제약조건 안됨
    - `firstname = ?0 or (firstname = ?1 and lastname = ?2)`
  - 매칭 조건이 매우 단순함
    - 문자는 `starts/contains/ends/regex`
    - 다른 속성은 정확한 매칭(=)만 지원
- 정리 
  - 실무에서 사용하기에는 매칭 조건이 너무 단순하고, LEFT 조인이 안됨
  - 실무에서는 `QueryDSL`을 사용하자
