# SQL function 호출하기

- `SQL function`은 `JPA`와 같이 `Dialect`에 등록된 내용만 호출할 수 있다.


### member ➡️ M으로 변경하는 replace 함수 사용

```java
@Test
@Commit
public void sqlFunction(){
    List<String> result = queryFactory
            .select(
                    Expressions
                            .stringTemplate("function('replace', {0}, {1}, {2})",
                                    member.username, "member", "M"))
            .from(member)
            .fetch();

    for (String s : result) {
        System.out.println("s = " + s);
    }
}
```

### 소문자로 변경해서 비교해라.

```java
@Test
public void sqlFunctionLowerCase(){
    List<String> result = queryFactory
            .select(member.username)
            .from(member)
            .where(member.username.eq
                    (Expressions.stringTemplate("function('lower',{0})", "MEMBER1")))
            .fetch();

    for (String s : result) {
        System.out.println("s = " + s);
    }
}
```
- `lower` 같은 `ansi` 표준 함수들은 `querydsl`이 상당부분 내장하고 있다.
- `.where(member.username.eq(member.username.lower()))`

Querydsl 내장 lower 적용하기 
```java
@Test
public void sqlFunctionLowerCase(){
    List<String> result = queryFactory
            .select(member.username)
            .from(member)
            .where(member.username.eq(member.username.lower()))
            .fetch();
    for (String s : result) {
        System.out.println("s = " + s);
    }
}
```
