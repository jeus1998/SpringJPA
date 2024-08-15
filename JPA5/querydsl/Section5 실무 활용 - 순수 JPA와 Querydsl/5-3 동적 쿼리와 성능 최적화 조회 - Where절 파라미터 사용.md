# 동적 쿼리와 성능 최적화 조회 - Where절 파라미터 사용

### Where절에 파라미터를 사용한 예제

```java
public List<MemberTeamDto> search(MemberSearchCondition condition){
    return queryFactory
            .select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name))
            .from(member)
            .leftJoin(member.team, team)
            .where(usernameEq(condition.getUsername()),
                    teamNameEq(condition.getTeamName()),
                    ageGoe(condition.getAgeGoe()),
                    ageLoe(condition.getAgeLoe()))
            .fetch();
}
private BooleanExpression ageGoe(Integer ageGoe) {
    return ageGoe == null ? null : member.age.goe(ageGoe);
}
private BooleanExpression ageLoe(Integer ageLoe) {
    return ageLoe == null ? null : member.age.loe(ageLoe);
}
private BooleanExpression teamNameEq(String teamName) {
    return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
}
private BooleanExpression usernameEq(String username) {
    return StringUtils.hasText(username) ? member.username.eq(username) : null;
}
```

### 테스트 - 동적 쿼리 Where절 파라미터  

```java
@Test
public void searchTest2(){
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
    condition.setAgeLoe(20);
    condition.setTeamName("teamA");

    List<MemberTeamDto> result = memberJpaRepository.search(condition);
    for (MemberTeamDto memberTeamDto : result) {
        System.out.println("memberTeamDto = " + memberTeamDto);
    }

    assertThat(result).extracting("username")
            .containsExactly("member1", "member2");
}
```