# 순수 JPA 리포지토리와 Querydsl

### 순수 JPA 리포지토리

```java
@Repository
public class MemberJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }
    public void save (Member member){
        em.persist(member);
    }
    public Optional<Member> findById(Long id){
        return Optional.ofNullable(em.find(Member.class, id));
    }
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    public List<Member> findByUsername(String username){
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }
}
```

### 순수 JPA 리포지토리 테스트

```java
@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Test
    public void basicTest(){
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result = memberJpaRepository.findAll();
        assertThat(result).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername(member.getUsername());
        assertThat(result2).containsExactly(member);
    }
}
```

## Querydsl 사용

### 순수 JPA 리포지토리 - Querydsl 추가

```java
public List<Member> findAll_Querydsl(){
    return queryFactory
            .selectFrom(member)
            .fetch();
}

public List<Member> findByUsername_Querydsl(String username){
    return queryFactory
            .selectFrom(member)
            .where(member.username.eq(username))
            .fetch();
}
```

### Querydsl 테스트 추가

```java
@Test
public void basicQuerydslTest(){
    Member member = new Member("member1", 10);
    memberJpaRepository.save(member);

    List<Member> result = memberJpaRepository.findAll_Querydsl();
    assertThat(result).containsExactly(member);

    List<Member> result2 = memberJpaRepository.findByUsername_Querydsl(member.getUsername());
    assertThat(result2).containsExactly(member);
}
```

### JPAQueryFactory 스프링 빈 등록

- 다음과 같이 `JPAQueryFactory`를 스프링 빈으로 등록해서 주입받아 사용해도 된다.

```java
@Bean
JPAQueryFactory jpaQueryFactory(EntityManager em) {
    return new JPAQueryFactory(em);
}
```

참고 - 동시성 문제 
- 동시성 문제는 걱정하지 않아도 된다.
- 왜냐하면 여기서 스프링이 주입해주는 엔티티 매니저는 실제 동작 시점에 진짜 엔티티 매니저를 찾아주는 프록시용 가짜 엔티티 매니저이다.
- 이 가짜 엔티티 매니저는 실제 사용 시점에 트랜잭션 단위로 실제 엔티티 매니저(영속성 컨텍스트)를 할당해준다.
- 