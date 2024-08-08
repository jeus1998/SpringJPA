# 스프링 데이터 JPA와 DB 설정, 동작확인

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/datajpa
    username: sa
    password:
    driver-class-name: org.h2.Driver

  jpa:
    hibernate:
      ddl-auto: create
    open-in-view: true
    properties:
      hibernate:
        # show_sql: true 
        format_sql: true
logging:
  level:
    org.hibernate.SQL: debug
  # org.hibernate.type: trace  
```
- `spring.jpa.hibernate.ddl-auto: create`
  - 이 옵션은 애플리케이션 실행 시점에 테이블을 drop 하고, 다시 생성한다.
- 참고: 모든 로그 출력은 가급적 로거를 통해 남겨야 한다.
  - `show_sql` : 옵션은 `System.out` 에 하이버네이트 실행 `SQL`을 남긴다.
  - `org.hibernate.SQL` : 옵션은 `logger`를 통해 하이버네이트 실행 `SQL`을 남긴다.
  - `org.hibernate.type: trace`: 옵션은 SQL 파라미터 바인딩을 보여준다. 

### 회원 엔티티 - Member

```java
@Entity
@Getter 
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;
    private String username;
    public Member(String username) {
        this.username = username;
    }
}

```

### 회원 JPA 리포지토리 - MemberJpaRepository

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
    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
```

### JPA 기반 테스트 - MemberJpaRepositoryTest

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
        assertThat(findMember).isEqualTo(member); // 같은 트랜잭션에서 JPA 엔티티 동일성 보장 - 1차 캐시 
    }
}
```

### 스프링 데이터 JPA 리포지토리 - MemberRepository

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

### 스프링 데이터 JPA 기반 테스트 - MemberRepositoryTest

```java
@Transactional
@Rollback(false)
@SpringBootTest
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
}
```

### 쿼리 파라미터 로그 남기기

- 로그에 다음을 추가하기 `org.hibernate.type` : SQL 실행 파라미터를 로그로 남긴다
- 외부 라이브러리 사용
  - https://github.com/gavlyukovskiy/spring-boot-data-source-decorator
- 스프링 부트를 사용하면 이 라이브러리만 추가하면 된다
  - `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'`
- 참고: 쿼리 파라미터를 로그로 남기는 외부 라이브러리는 시스템 자원을 사용하므로, 개발 단계에서는 편하게 사용해도 된다. 
  하지만 운영시스템에 적용하려면 꼭 성능테스트를 하고 사용하는 것이 좋다.


