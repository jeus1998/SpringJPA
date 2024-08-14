# Case 문

- `select`, 조건절(where), `order by`에서 사용 가능

### 단순한 조건 

```java
@Test
public void basicCase(){
    List<String> result = queryFactory
            .select(member.age
                    .when(10).then("열살")
                    .when(20).then("스무살")
                    .otherwise("늙은이"))
            .from(member)
            .fetch();
    for (String s : result) {
        System.out.println("s = " + s);
    }
}
```

### 복잡한 조건 

```java
@Test
public void complexCase(){
    List<String> result = queryFactory
            .select(
                    new CaseBuilder()
                            .when(member.age.between(0, 20)).then("0~20살")
                            .when(member.age.between(21, 30)).then("21~30살")
                            .otherwise("기타"))
            .from(member)
            .fetch();
    for (String s : result) {
        System.out.println("s = " + s);
    }
}
```

### orderBy에서 Case 문 함께 사용하기 예제

- 예를 들어서 다음과 같은 임의의 순서로 회원을 출력하고 싶다면?
- 0 ~ 30살이 아닌 회원을 가장 먼저 출력
- 0 ~ 20살 회원 출력
- 21 ~ 30살 회원 출력

```java
 /**
 * 0 ~ 30살이 아닌 회원을 가장 먼저 출력
 * 0 ~ 20살 회원 출력
 * 21 ~ 30살 회원 출력
 */
@Test
public void complexCase2(){
    NumberExpression<Integer> rank = new CaseBuilder()
            .when(member.age.between(0, 20)).then(1)
            .when(member.age.between(21, 30)).then(2)
            .otherwise(0);

    List<Member> result = queryFactory
            .select(member)
            .from(member)
            .orderBy(rank.asc(), member.age.desc())
            .fetch();
    for (Member member : result) {
        System.out.println("member = " + member);
    }
}
```
- `Querydsl`은 자바 코드로 작성하기 때문에 `rank` 처럼 복잡한 조건을 변수로 선언해서 `select 절`, `orderBy 절`에서 
  함께 사용할 수 있다.




