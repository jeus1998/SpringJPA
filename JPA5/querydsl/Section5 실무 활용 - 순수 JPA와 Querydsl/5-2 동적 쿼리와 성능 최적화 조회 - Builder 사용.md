# 동적 쿼리와 성능 최적화 조회 - Builder 사용

### MemberTeamDto - 조회 최적화용 DTO 추가

```java
@Data
public class MemberTeamDto {
    private Long memberId;
    private String username;
    private int age;
    private Long teamId;
    private String teamName;
    @QueryProjection
    public MemberTeamDto(Long memberId, String username, int age, Long teamId, String teamName) {
        this.memberId = memberId;
        this.username = username;
        this.age = age;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
```
- `@QueryProjection`을 추가했다.
- `QMemberTeamDto` 생성을 위해서 gradle clean & gradle build
- 참고: `@QueryProjection`을 사용하면 해당 `DTO`가 `Querydsl`을 의존하게 된다.
  이런 의존이 싫으면, `Projection.bean(), fields(), constructor()`을 사용하면 된다.

### 회원 검색 조건

```java
@Data
public class MemberSearchCondition {
    // 회원명, 팀명, 나이(ageGoe, ageLoe)
    private String username;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;
}
```
- 이름이 너무 길면 `MemberCond` 등으로 줄여 사용해도 된다.

### 동적쿼리 - Builder 사용

```java
public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition){
    BooleanBuilder builder = new BooleanBuilder();

    if(StringUtils.hasText(condition.getUsername())){
        builder.and(member.username.eq(condition.getUsername()));
    }
    if(StringUtils.hasText(condition.getTeamName())){
        builder.and(team.name.eq(condition.getTeamName()));
    }
    if(condition.getAgeGoe() != null){
        builder.and(member.age.goe(condition.getAgeGoe()));
    }
    if(condition.getAgeLoe() != null){
        builder.and(member.age.loe(condition.getAgeLoe()));
    }
    
    return queryFactory
            .select(new QMemberTeamDto(
                    member.id, member.username, member.age, team.id , team.name))
            .from(member)
            .leftJoin(member.team, team)
            .where(builder)
            .fetch();
}
```

### 조회 예제 테스트

```java
@Test
public void searchTest(){
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

    MemberSearchCondition condition = new MemberSearchCondition();
    condition.setAgeGoe(35);
    condition.setAgeLoe(40);
    condition.setTeamName("teamB");

    List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
    for (MemberTeamDto memberTeamDto : result) {
        System.out.println("memberTeamDto = " + memberTeamDto);
    }

    assertThat(result).extracting("username")
            .containsExactly("member4");
}
```