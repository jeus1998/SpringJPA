# 순수 JPA 기반 리포지토리 만들기

- 순수한 JPA 기반 리포지토리를 만들자
- 기본 CRUD
  - 저장
  - 변경 ➡️ 변경감지 사용
  - 삭제
  - 전체 조회
  - 단건 조회
  - 카운트

### 순수 JPA 기반 리포지토리 - 회원 - MemberJpaRepository

```java
@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    // @PersistenceContext
    private final EntityManager em;
    public Member save(Member member){
        em.persist(member);
        return member;
    }
    public void delete(Member member){
        em.remove(member);
    }
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
    public Optional<Member> findById(Long id){
        return Optional.ofNullable(em.find(Member.class, id));
    }
    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }
    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
```

### 순수 JPA 기반 리포지토리 - 팀 - TeamJpaRepository

```java
@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {
    private final EntityManager em;
    public Team save(Team team){
        em.persist(team);
        return team;
    }
    public void delete(Team team){
        em.remove(team);
    }
    public List<Team> findAll(){
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }
    public Optional<Team> findById(Long id){
        return Optional.ofNullable(em.find(Team.class, id));
    }
    public Long count(){
        return em.createQuery("select count(t) from Team t", Long.class).getSingleResult();
    }
}
```

### 순수 JPA 기반 리포지토리 테스트 - MemberJpaRepositoryTest 추가 

```java
@Transactional
@Rollback(false)
@SpringBootTest
class MemberJpaRepositoryTest {
    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        // 다시 카운트 검증
        assertThat(memberJpaRepository.count()).isEqualTo(0);
    }
}
```
- 기본 CRUD를 검증한다.

