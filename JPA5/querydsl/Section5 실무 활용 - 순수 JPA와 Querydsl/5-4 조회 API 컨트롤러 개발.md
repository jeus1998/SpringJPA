# 조회 API 컨트롤러 개발

### 프로파일 설정

src/main/resources/application.yml
```yaml
spring:
  profiles:
    active: local

# 나머지 생략 ..
```

src/test/resources/application.yml
```yaml
spring:
  profiles:
    active: test

# 나머지 생략 ..
```

- 이렇게 분리하면 main 소스코드와 테스트 소스 코드 실행시 프로파일을 분리할 수 있다.

### 샘플 데이터 추가 - InitMember

```java
@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {
    private final InitMemberService initMemberService;
    @PostConstruct
    public void init(){
        initMemberService.init();
    }
    @Component
    @RequiredArgsConstructor
    static class InitMemberService{
        @PersistenceContext
        private final EntityManager em;
        @Transactional
        public void init(){
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i < 100; i++) {
                Team selectedTeam = i % 2 == 0 ? teamA : teamB;
                em.persist(new Member("member" + i, i, selectedTeam));
            }
        }
    }
}
```
- `@PostConstruct` 부분과 `@Transactional`부분을 분리해서 실행해야 한다.  

### 조회 컨트롤러

```java
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberJpaRepository memberJpaRepository;
    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition){
        return memberJpaRepository.search(condition);
    }
}
```
- `http://localhost:8080/v1/members?teamName=teamB&ageGoe=31&ageLoe=35`