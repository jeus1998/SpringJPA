# 동적 쿼리 - BooleanBuilder 사용

동적 쿼리를 해결하는 두가지 방식
- BooleanBuilder
- Where 다중 파라미터 사용

### BooleanBuilder

```java
@Test
public void dynamicQuery_BooleanBuilder(){
    String usernameParam = "member1";
    Integer ageParam = 10;

    List<Member> result = searchMember1(usernameParam, ageParam);
    assertThat(result.size()).isEqualTo(1);
}

private List<Member> searchMember1(String usernameParam, Integer ageParam) {
    BooleanBuilder builder = new BooleanBuilder();
    if(usernameParam != null){
        builder.and(member.username.eq(usernameParam));
    }
    if(ageParam != null){
        builder.and(member.age.eq(ageParam));
    }

    return queryFactory
            .selectFrom(member)
            .where(builder)
            .fetch();
}
```