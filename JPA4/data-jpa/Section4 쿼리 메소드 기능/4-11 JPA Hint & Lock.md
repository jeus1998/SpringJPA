# JPA Hint & Lock

### JPA Hint

- JPA 쿼리 힌트(SQL 힌트가 아니라 JPA 구현체에게 제공하는 힌트)

쿼리 힌트 사용
```java
@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
Member findReadOnlyByUsername(String username);
```

쿼리 힌트 사용 확인
```java
@Test
public void queryHint(){
    // given
    Member member = new Member("member1", 10);
    memberRepository.save(member);
    em.flush();
    em.clear();

    // when
    Member findMember = memberRepository.findReadOnlyByUsername("member1");
    findMember.setUsername("member2"); // 변경 감지(dirty checking) 

    em.flush(); // readOnly여서 Update Query 실행 X
}
```

### 쿼리 힌트 Page 추가 예제

```java
@QueryHints(value = { @QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)
Page<Member> findByUsername(String name, Pageable pageable);
```
- `org.springframework.data.jpa.repository.QueryHints` 어노테이션을 사용
- `forCounting`: 반환 타입으로 Page 인터페이스를 적용하면 추가로 호출하는 페이징을 위한 `count 쿼리`도 쿼리 힌트 적용(기본값 true)

### Lock

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
List<Member> findByUsername(String name);
```
- `org.springframework.data.jpa.repository.Lock` 어노테이션을 사용
- `JPA`가 제공하는 락은 JPA 책 16.1 트랜잭션과 락 절을 참고


