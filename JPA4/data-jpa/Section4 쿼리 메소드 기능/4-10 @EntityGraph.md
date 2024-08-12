# @EntityGraph

- 연관된 엔티티들을 SQL 한번에 조회하는 방법

### 지연 로딩과 N+1 문제 

- `member` ➡️ `team`은 지연로딩 관계이다.
- 따라서 다음과 같이 `team`의 데이터를 조회할 때 마다 쿼리가 실행된다. 
  -  N+1 문제 발생

```java
@Test
public void findMemberLazy(){
    // given
    // member1 -> teamA
    // member2 -> teamB

    Team teamA = new Team("teamA");
    teamRepository.save(teamA);
    Team teamB = new Team("teamB");
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10);
    member1.changeTeam(teamA);
    memberRepository.save(member1);

    Member member2 = new Member("member2", 20);
    member2.changeTeam(teamB);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    List<Member> members = memberRepository.findAll();
    // N+1 문제
    for (Member member : members) {
        System.out.println("member = " + member);
        Team team = member.getTeam();
        System.out.println(Persistence.getPersistenceUtil().isLoaded(team)); // 프록시, false
        System.out.println("team = " + team);
    }
}
```

실행된 쿼리 
```sql
// 1
 select
    m1_0.member_id,
    m1_0.age,
    m1_0.team_id,
    m1_0.username 
from
    member m1_0

// N
select
   t1_0.team_id,
   t1_0.name 
from
   team t1_0 
where
   t1_0.team_id=?        

select
    t1_0.team_id,
    t1_0.name 
from
    team t1_0 
where
    t1_0.team_id=?
```


참고 다음과 같이 지연 로딩 여부를 확인할 수 있다.
```java
//Hibernate 기능으로 확인
Hibernate.isInitialized(member.getTeam());

//JPA 표준 방법으로 확인
PersistenceUnitUtil util =
em.getEntityManagerFactory().getPersistenceUnitUtil();
util.isLoaded(member.getTeam());
```

### JPQL 페치 조인

```java
@Query("select m from Member m left join fetch m.team")
List<Member> findMemberFetchJoin();
```

```java
@Test
public void findMemberFetchJoin(){
    // given
    // member1 -> teamA
    // member2 -> teamB

    Team teamA = new Team("teamA");
    teamRepository.save(teamA);
    Team teamB = new Team("teamB");
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10);
    member1.changeTeam(teamA);
    memberRepository.save(member1);

    Member member2 = new Member("member2", 20);
    member2.changeTeam(teamB);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    List<Member> members = memberRepository.findMemberFetchJoin();
    // N+1 문제해결 by 패치 조인
    for (Member member : members) {
        System.out.println("member = " + member);
        Team team = member.getTeam();
        System.out.println(Persistence.getPersistenceUtil().isLoaded(team)); // true
        System.out.println("team = " + team);
    }
}
```

실행된 쿼리 
```sql
select
    m1_0.member_id,
    m1_0.age,
    t1_0.team_id,
    t1_0.name,
    m1_0.username 
from
    member m1_0 
left join
    team t1_0 
        on t1_0.team_id=m1_0.team_id
```

### EntityGraph

- 스프링 데이터 `JPA`는 `JPA`가 제공하는 엔티티 그래프 기능을 편리하게 사용하게 도와준다.
- 이 기능을 사용하면 `JPQL` 없이 페치 조인을 사용할 수 있다. (JPQL + 엔티티 그래프도 가능)

```java
@Override
@EntityGraph(attributePaths = {"team"})
List<Member> findAll();
```

```java
@Test
public void findMemberFetchJoin(){
    // given
    // member1 -> teamA
    // member2 -> teamB

    Team teamA = new Team("teamA");
    teamRepository.save(teamA);
    Team teamB = new Team("teamB");
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10);
    member1.changeTeam(teamA);
    memberRepository.save(member1);

    Member member2 = new Member("member2", 20);
    member2.changeTeam(teamB);
    memberRepository.save(member2);

    em.flush();
    em.clear();
    
    List<Member> members = memberRepository.findAll();

    // N+1 문제해결 by 엔티티 그래프
    for (Member member : members) {
        System.out.println("member = " + member);
        Team team = member.getTeam();
        System.out.println(Persistence.getPersistenceUtil().isLoaded(team)); // true
        System.out.println("team = " + team);
    }
}
```
- 사실상 페치 조인(FETCH JOIN)의 간편 버전
- LEFT OUTER JOIN 사용

엔티티 그래프 실행된 쿼리 
```sql
select
    m1_0.member_id,
    m1_0.age,
    t1_0.team_id,
    t1_0.name,
    m1_0.username 
from
    member m1_0 
left join
    team t1_0 
        on t1_0.team_id=m1_0.team_id
```

### 다양한 엔티티 그래프 활용 

```java
/**
 * JPQL + 엔티티 그래프
 */
@EntityGraph(attributePaths = {"team"})
@Query("select m from Member m")
List<Member> findMemberEntityGraph();

/**
 * 메서드 이름 + 엔티티 그래프
 */
@EntityGraph(attributePaths = {"team"})
List<Member> findEntityGraphByUsername(@Param("username") String username);
```