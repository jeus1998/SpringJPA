# 인터페이스 지원 - QuerydslPredicateExecutor

- [공식 URL](https://docs.spring.io/spring-data/jpa/docs/2.2.3.RELEASE/reference/html/#core.extensions.querydsl)

### QuerydslPredicateExecutor 인터페이스 적용 

```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, QuerydslPredicateExecutor {
    List<Member> findByUsername(String username);
}
```

### QuerydslPredicateExecutor 인터페이스

```java
public interface QuerydslPredicateExecutor<T> {
     Optional<T> findById(Predicate predicate); 
     Iterable<T> findAll(Predicate predicate); 
     long count(Predicate predicate); 
     boolean exists(Predicate predicate); 
     // … more functionality omitted.
}
```

### 사용법 

```java
@Test
public void querydslPredicateExecutorTest(){
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    em.persist(teamA);
    em.persist(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 20, teamA);
    Member member3 = new Member("member3", 30, teamB);
    Member member4 = new Member("member4", 40, teamB);
    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(member4);
    
    // Q-type 사용이 가능하다 
    Iterable<Member> result = memberRepository.findAll(member.age.between(20, 40).and(member.username.eq("member2")));
    for (Member member : result) {
        System.out.println("member = " + member);
    }
}
```

### 한계점

- 조인 ❌ (묵시적 조인은 가능하지만 `left join`이 불가능하다.)
- 클라이언트가 `Querydsl`에 의존해야 한다. 서비스 클래스가 `Querydsl`이라는 구현 기술에 의존해야 한다.
- 복잡한 실무환경에서 사용하기에는 한계가 명확하다.