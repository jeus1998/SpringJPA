# 프로젝션과 결과 반환 - @QueryProjection

### MemberDto: 생성자 + @QueryProjection

```java
@Data
@NoArgsConstructor
public class MemberDto {
    private String username;
    private int age;
    @QueryProjection
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
```
- clean: gradle ➡️ Tasks ➡️ build ➡️ clean 
- build: gradle ➡️ Tasks ➡️ build ➡️ build
- reload all gradle projects: gradle ➡️ 클릭(🔄)
- Q-Type 생성확인: root 파일 ➡️ build ➡️ generated ➡️ source ➡️ annotationProcessor ➡️ QMemberDto

### @QueryProjection 활용 & 트레이드 오프 

```java
@Test
public void findDtoByQueryProjection(){
    List<MemberDto> result = queryFactory
            .select(new QMemberDto(member.username, member.age))
            .from(member)
            .fetch();

    for (MemberDto memberDto : result) {
        System.out.println("memberDto = " + memberDto);
    }
}
```
- 이 방법은 컴파일러로 타입을 체크할 수 있으므로 가장 안전한 방법이다
- 다만 `DTO`에 QueryDSL 어노테이션을 유지해야 하는 점과 `DTO`까지 Q 파일을 생성해야 하는 단점이 있다.
- 이런 이유로 DTO 스펙이 바뀌면 build 다시해야함 (Q 파일 재생성을 위해서)
- 아키텍처 관점 
  - DTO 같은 경우 Repository, Service, Controller 모두 사용한다. 
  - 근데 `@QueryProjection` 애노테이션으로 해당 `DTO`가 `Querydsl`에 의존하게 된다.
  - 예시로 두개의 `projection`을 select 하면 `Querydsl`의 `tuple type`으로 반환하게 되는데
    이 또한 `Querydsl`에 의존하지 않기 위해서 repository 계층에서만 사용하고 서비스에 반환은 
    `DTO`로 하는게 좋다고 다시 `DTO`로 변환하여 반환했다. 
  - 결론은 애플리케이션 자체가 `Querydsl`에 많이 의존하고 바뀔 기술이 아니라고 하면 `DTO`에 포함을 하고 
    아니라면 Projections(프로퍼티, 생성자, 필드)방식을 사용해서 순수하게 가져가면 된다. 



