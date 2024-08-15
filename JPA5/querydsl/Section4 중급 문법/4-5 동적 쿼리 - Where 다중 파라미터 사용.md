# 동적 쿼리 - Where 다중 파라미터 사용

```java
@Test
public void dynamicQuery_WhereParam(){
    String usernameParam = "member1";
    Integer ageParam = 10;

    List<Member> result = searchMember2(usernameParam, ageParam);
    assertThat(result.size()).isEqualTo(1);
}

private List<Member> searchMember2(String usernameParam, Integer ageParam) {
    return queryFactory
            .selectFrom(member)
            .where(usernameEq(usernameParam), ageEq(ageParam))
            .fetch();
}
// 3항 연산자 활용
private BooleanExpression ageEq(Integer ageParam) {
    return ageParam == null ? null : member.age.eq(ageParam);
}
private BooleanExpression usernameEq(String usernameParam) {
    if(usernameParam != null){
        return member.username.eq(usernameParam);
    }
    return null;
}
// 조립 가능
private BooleanExpression allEq(String usernameParam, Integer ageParam){
    return usernameEq(usernameParam).and(ageEq(ageParam));
}
```
- where 조건에 null 값은 무시된다.
- 메서드를 다른 쿼리에서도 재활용 할 수 있다.
- 쿼리 자체의 가독성이 높아진다.
- 조합이 가능하다. 