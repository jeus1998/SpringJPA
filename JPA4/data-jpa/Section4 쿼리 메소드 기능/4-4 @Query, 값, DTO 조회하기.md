# @Query, 값, DTO 조회하기

### 단순히 값 하나를 조회

```java
@Query("select m.username from Member m")
List<String> findUsernameList();
```
- JPA 값 타입(@Embedded)도 이 방식으로 조회할 수 있다.

### MemberDto

```java
@Getter
@ToString(of = {"id", "username", "teamName"})
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;
    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
}
```

### DTO로 직접 조회

```java
@Query("select new spring.data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
List<MemberDto> findMemberDto();
```
- `DTO`로 직접 조회 하려면 `JPA`의 `new 명령어`를 사용해야 한다.
- 그리고 생성자가 있는 `DTO`가 필요 하다. (JPA와 사용방식이 동일하다.)

### 테스트 

```java
@Test
public void findUsernameList(){
    Member m1 = new Member("AAA", 10);
    Member m2 = new Member("BBB", 20);
    memberRepository.save(m1);
    memberRepository.save(m2);

    List<String> result = memberRepository.findUsernameList();
    assertThat(result).contains("AAA", "BBB");
    for (String name : result) {
        System.out.println("name = " + name);
    }
}
@Test
public void findMemberDto(){
    Team team = new Team("teamA");
    teamRepository.save(team);

    Member member = new Member("AAA", 10);
    member.changeTeam(team);
    memberRepository.save(member);

    List<MemberDto> memberDto = memberRepository.findMemberDto();
    for (MemberDto dto : memberDto) {
        System.out.println("dto = " + dto);
    }
}
```