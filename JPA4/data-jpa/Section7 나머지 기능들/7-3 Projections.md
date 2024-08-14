# Projections

- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections
- 엔티티 대신에 DTO를 편리하게 조회할 때 사용
- 전체 엔티티가 아니라 만약 회원 이름만 딱 조회하고 싶으면?

### UsernameOnly - 인터페이스 

```java
public interface UserNameOnly {
    String getUsername();
}
```
- 조회할 엔티티의 필드를 `getter`형식으로 지정하면 해당 필드만 선택해서 조회(Projection)

### MemberRepository extends JpaRepository 추가 

```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<UserNameOnly> findProjectionsByUsername(@Param("username") String username);
}
```
- 메서드 이름은 자유, 반환 타입으로 인지

### 테스트 

```java
@Test
public void projections(){
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
    List<UserNameOnly> result = memberRepository.findProjectionsByUsername("m1");

    for (UserNameOnly userNameOnly : result) {
        System.out.println("userNameOnly = " + userNameOnly);
    }
}
```

실행된 쿼리 
```sql
select
    m1_0.username 
from
    member m1_0 
where
    m1_0.username=?
```
- `SQL`에서도 `select`절에서 `username`만 조회(Projection)하는 것을 확인

### 인터페이스 기반 Closed Projections

- 프로퍼티 형식(getter)의 인터페이스를 제공하면, 구현체는 스프링 데이터 JPA가 제공
```java
public interface UserNameOnly {
    String getUsername();
}
```

### 인터페이스 기반 Open Projections

- 다음과 같이 스프링의 SpEL 문법도 지원
```java
public interface UserNameOnly {
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
```
- 단! 이렇게 `SpEL`문법을 사용하면, DB에서 엔티티 필드를 다 조회해온 다음에 계산한다!
- 따라서 JPQL SELECT 절 최적화가 안된다

### 클래스 기반 Projection

- 다음과 같이 인터페이스가 아닌 구체적인 DTO 형식도 가능
- 생성자의 파라미터 이름으로 매칭
```java
@Getter
public class UsernameOnlyDto {
    private final String username;
    public UsernameOnlyDto(String username) {
        this.username = username;
    }
}
```

MemberRepository 추가 
```java
List<UsernameOnlyDto> findClassProjectionsByUsername(@Param("username") String username);
```

테스트 
```java
@Test
public void projections3(){
    //given
    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member m1 = new Member("m1", 0, teamA);
    Member m2 = new Member("m2", 0, teamA);
    em.persist(m1);
    em.persist(m2);

    em.flush();
    em.clear();

    List<UsernameOnlyDto> result = memberRepository.findClassProjectionsByUsername("m2");
    for (UsernameOnlyDto usernameOnlyDto : result) {
        System.out.println(usernameOnlyDto.getUsername());
        System.out.println(usernameOnlyDto.getClass());
    }
}
```

실행된 쿼리 
```sql
select
    m1_0.username 
from
    member m1_0 
where
    m1_0.username=?
```

### 동적 Projections

MemberRepository 추가 
```java
<T> List<T> findProjectionGenericByUsername(@Param("username") String username, Class<T> type);
```

사용코드
```java
List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1",UsernameOnly.class);
```

### 중첩 구조 처리

```java
public interface NestedClosedProjections {
    String getUsername();
    TeamInfo getTeam();
    interface TeamInfo {
        String getName();
    }
}
```

테스트 
```java
@Test
public void genericAndNested(){
    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member m1 = new Member("m1", 0, teamA);
    Member m2 = new Member("m2", 0, teamA);
    em.persist(m1);
    em.persist(m2);

    em.flush();
    em.clear();

    List<NestedClosedProjections> result = memberRepository.findProjectionGenericByUsername("m1", NestedClosedProjections.class);
    for (NestedClosedProjections nestedClosedProjections : result) {
        System.out.println(nestedClosedProjections.getUsername());
        System.out.println(nestedClosedProjections.getTeam().getName());
    }
}
```

실행된 쿼리 
```sql
select
    m1_0.username,
    t1_0.team_id,
    t1_0.name 
from
    member m1_0 
left join
    team t1_0 
        on t1_0.team_id=m1_0.team_id 
where
    m1_0.username=?
```
- 프로젝션 대상이 root 엔티티면, JPQL SELECT 절 최적화 가능
- 프로젝션 대상이 ROOT가 아니면
  - LEFT OUTER JOIN 처리
  - 모든 필드를 SELECT해서 엔티티로 조회한 다음에 계산

### 정리 

- 프로젝션 대상이 root 엔티티면 유용하다.
- 프로젝션 대상이 root 엔티티를 넘어가면 JPQL SELECT 최적화가 안된다!
- 실무의 복잡한 쿼리를 해결하기에는 한계가 있다.
- 실무에서는 단순할 때만 사용하고, 조금만 복잡해지면 `QueryDSL`을 사용하자


