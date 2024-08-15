# 스프링 데이터 JPA 리포지토리로 변경

### 스프링 데이터 JPA - MemberRepository 생성

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsername(String username);
}
```

### 스프링 데이터 JPA 테스트

```java
@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Test
    public void basicTest(){
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result = memberRepository.findAll();
        assertThat(result).containsExactly(member);

        List<Member> result2 = memberRepository.findByUsername(member.getUsername());
        assertThat(result2).containsExactly(member);
    }
}
```
- `Querydsl` 전용 기능인 회원 `search`를 작성할 수 없다. ➡️ 사용자 정의 리포지토리 필요

