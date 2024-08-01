# JPA와 DB 설정, 동작확인

### application.yml - JPA & DB 설정 

```yaml
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/jpashop
    username: sa
    password:
    driver-class-name: org.h2.Driver

  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
#        show_sql: true
        format_sql: true

logging:
  level:
    org.hibernate.SQL: debug
    org.hibernate.orm.jdbc.bind: trace  #스프링 부트 3.x, hibernate6

```
- `spring.jpa.hibernate.ddl-auto: create`
  - 이 옵션은 애플리케이션 실행 시점에 테이블을 drop 하고, 다시 생성한다.
- 참고: 모든 로그 출력은 가급적 로거를 통해 남겨야 한다
  - `show_sql` : 옵션은 `System.out` 에 하이버네이트 실행 `SQL`을 남긴다.
  - `org.hibernate.SQL` : 옵션은 `logger`를 통해 하이버네이트 실행 `SQL`을 남긴다.
  - 

주의!
- `application.yml` 같은 `yml` 파일은 띄어쓰기(스페이스) 2칸으로 계층을 만듭니다.
- 따라서 띄어쓰기 2칸을 필수로 적어주어야 합니다.

## 실제 동작하는지 확인하기

### 회원 엔티티 - Member

```java
@Entity
@Getter
@Setter
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String username;
}
```

### 회원 리포지토리 - MemberRepository

```java
@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }
    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
```
- `implementation 'org.springframework.boot:spring-boot-starter-data-jpa'`
- 해당 의존성을 주입하면 `EntityManager`를 주입 받을 수 있다.

### 테스트 - MemberRepositoryTest

```java
@RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Test
    @Transactional           // JPA DB 데이터 변경(삽입)은 애플리케이션 트랜잭션 단위에서 실행 해야함
    @Rollback(value = false) // 테스트에 @Transactional 붙으면 기본이 true
    public void testMember() throws Exception{
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        System.out.println("findMember == member: " + (findMember == member));
    }
}
```

참고
- 스프링 부트를 통해 복잡한 설정이 다 자동화 되었다. 
- `persistence.xml` 도 없다.
- `LocalContainerEntityManagerFactoryBean` 도 없다
- ⭐️ `@RunWith(SpringRunner.class)`
  - `JUnit4`와 함께 사용되는 `Spring TestContext Framework`의 일부
  -  Spring 컨텍스트를 JUnit 테스트에서 사용하기 위해 필요
  - 하지만 `Spring Boot 2.1.0` 이후부터는 `@SpringBootTest`를 사용할 때 자동으로 `SpringRunner` 또는 `SpringExtension`을 
    사용하는 것으로 간주되어 별도로 `@RunWith(SpringRunner.class)`를 명시하지 않아도 `Spring 컨텍스트`가 로드
- `JUnit 4` vs `JUnit 5`
  - JUnit 4: `@RunWith(SpringRunner.class)`를 사용하여 `Spring`의 `TestContext 프레임워크`를 활성화
  - JUnit 5: `@ExtendWith(SpringExtension.class)`를 사용하여 동일한 기능을 수행
    - `@SpringBootTest`와 함께 사용하면 S`pringExtension`이 자동으로 적용


### 쿼리 파라미터 로그 남기기

- Yaml 추가 
  - 로그에 다음을 추가하기: SQL 실행 파라미터를 로그로 남긴다.
  - 스프링 부트 2.x, hibernate5
    - `org.hibernate.type: trace`
  - 스프링 부트 3.x, hibernate6
    - `org.hibernate.orm.jdbc.bind: trace`
- build.gradle 추가
  - `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'`
  - 쿼리 파라미터를 로그로 남기는 외부 라이브러리는 시스템 자원을 사용하므로, 개발 단계에서는 편하게 사용해도 된다.
  - 하지만 운영시스템에 적용하려면 꼭 성능테스트를 하고 사용하는 것이 좋다