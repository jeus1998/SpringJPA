# JPA NamedQuery

### @NamedQuery 어노테이션으로 Named 쿼리 정의

```java
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;
    // 생략 ...
}    
```

### JPA를 직접 사용해서 Named 쿼리 호출

```java
public List<Member> findByUsername(String username){
    return em.createNamedQuery("Member.findByUsername", Member.class)
            .setParameter("username", username)
            .getResultList();
}
```

### 스프링 데이터 JPA로 NamedQuery 사용

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
}
```

@Query 생략 버전 
```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    List<Member> findByUsername(@Param("username") String username);
}
```
- `@Query` 를 생략하고 메서드 이름만으로 Named 쿼리를 호출할 수 있다.
- 스프링 데이터 JPA는 선언한 `"도메인 클래스 + .(점) + 메서드 이름"`으로 Named 쿼리를 찾아서 실행
- 만약 실행할 Named 쿼리가 없으면 메서드 이름으로 쿼리 생성 전략을 사용한다. 
- 필요하면 전략을 변경할 수 있지만 권장하지 않는다.
  - [참고](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-lookup-strategies)

참고
- 스프링 데이터 JPA를 사용하면 실무에서 `Named Query`를 직접 등록해서 사용하는 일은 드물다.
- 대신 `@Query`를 사용해서 리파지토리 메소드에 쿼리를 직접 정의한다.
- Named Query 장점
  - 직접 작성한 `JPQL`에 문법 오류가 있으면 사용자가 실행을 하는 시점에 에러가 발생한다. 
  - 하지만 `Named Query`는 문법 오류가 있으면 애플리케이션 로딩 시점에 에러가 발생한다. 
  - 제일 좋은 오류는 컴파일 오류 & 애플리케이션 로딩 시점 오류이다. 

