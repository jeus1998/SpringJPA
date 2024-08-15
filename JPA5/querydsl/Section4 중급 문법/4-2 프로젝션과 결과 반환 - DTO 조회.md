# 프로젝션과 결과 반환 - DTO 조회

### MemberDto

```java
@Data
public class MemberDto {
    private String username;
    private int age;
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
```

### 순수 JPA에서 DTO 조회 코드

```java
@Test
public void findDtoByJPQL(){
    List<MemberDto> result =
            em.createQuery("select new spring.querydsl.dto.MemberDto(m.username, m.age)" +
                    "from Member m", MemberDto.class)
            .getResultList();

    for (MemberDto memberDto : result) {
        System.out.println("memberDto = " + memberDto);
    }
}
```
- 순수 JPA에서 DTO를 조회할 때는 new 명령어를 사용해야함
- DTO의 package 이름을 다 적어줘야해서 지저분함
- 생성자 방식만 지원

### Querydsl 빈 생성(Bean population)

- 결과를 DTO 반환할 때 사용
- 다음 3가지 방법 지원
  - 프로퍼티 접근
  - 필드 직접 접근
  - 생성자 사용
- 해당 DTO에 기본 생성자가 필요 
  - MemberDto - `@NoArgsConstructor` 추가

프로퍼티 접근 - Setter
```java
@Test
public void findDtoBySetter(){
    List<MemberDto> result = queryFactory
            .select(Projections.bean(MemberDto.class,
                    member.username,
                    member.age))
            .from(member)
            .fetch();
    for (MemberDto memberDto : result) {
        System.out.println("memberDto = " + memberDto);
    }
}
```
- setter 필요 ⭕️

필드 직접 접근
```java
@Test
public void findDtoByFields(){
    List<MemberDto> result = queryFactory
            .select(Projections.fields(MemberDto.class,
                    member.username,
                    member.age))
            .from(member)
            .fetch();
    for (MemberDto memberDto : result) {
        System.out.println("memberDto = " + memberDto);
    }
}
```
- setter 필요 ❌

생성자를 통한 접근 
```java
@Test
public void findDtoByConstructor(){
    List<MemberDto> result = queryFactory
            .select(Projections.constructor(MemberDto.class,
                    member.username,
                    member.age))
            .from(member)
            .fetch();
    for (MemberDto memberDto : result) {
        System.out.println("memberDto = " + memberDto);
    }
}
```

### 필드명이 다를 때 

UserDto 
```java
public class UserDto {
    private String name;
    private int age;
}
```

```java
@Test
public void findByNotSameFields(){
    List<UserDto> result = queryFactory
            .select(Projections.fields(UserDto.class,
                    member.username.as("name"),
                    member.age))
            .from(member)
            .fetch();

    for (UserDto userDto : result) {
        System.out.println("userDto = " + userDto);
    }
}
```
- 프로퍼티나, 필드 접근 생성 방식에서 이름이 다를 때 해결 방안
- `ExpressionUtils.as(source,alias)` : 필드나, 서브 쿼리에 별칭 적용
  - `member`의 필드는 username `User`의 필드는 name 
  - `member.username.as("name")`: 필드에 별칭 적용 


### ExpressionUtils.as(source,alias) + 서브 쿼리 적용 

```java
/**
* 각 row: 회원 이름 + 최대 나이
* 최대 나이를 위한 서브 쿼리 필요
*/
@Test
public void subQueryAlias(){
  QMember qm = new QMember("memberSub");

  List<UserDto> result = queryFactory
          .select(Projections.fields(UserDto.class,
                  member.username.as("name"),
                  ExpressionUtils.as(select(qm.age.max())
                          .from(qm), "age")))
          .from(member)
          .fetch();
  for (UserDto userDto : result) {
      System.out.println("userDto = " + userDto);
  }
}
```
- Projections.(files, constructor, bean): DTO 조회
- ExpressionUtils.as(source, alias): 필드나 서브쿼리에 별칭 적용 
- JpaExpressions: 서브 쿼리 