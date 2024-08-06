# 회원 조회 API


### 회원조회 V1: 응답 값으로 엔티티를 직접 외부에 노출

```java
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
}
```
- 문제점
  - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
  - 기본적으로 엔티티의 모든 값이 노출된다.
  - 응답 스펙을 맞추기 위해 로직이 추가된다. (@JsonIgnore, 별도의 뷰 로직 등등)
  - 실무에서는 같은 엔티티에 대해 `API`가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의 `API`를 
    위한 프레젠테이션 응답 로직을 담기는 어렵다
  - 엔티티가 변경되면 `API` 스펙이 변한다.
  - 추가로 컬렉션을 직접 반환하면 항후 `API` 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로 해결)
- 결론
  - `API` 응답 스펙에 맞추어 별도의 `DTO`를 반환한다.

참고
```text
엔티티를 외부에 노출하지 마세요!

실무에서는 member 엔티티의 데이터가 필요한 API가 계속 증가하게 된다.
어떤 API는 name 필드가 필요하지만, 어떤 API는 name 필드가 필요없을 수 있다.

결론적으로 엔티티 대신에 API 스펙에 맞는 별도의 DTO를 노출해야 한다.
```

### 회원조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO 사용

```java
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect, collect.size());
    }

  /**
   * @param <T> Result<List<MemberDto>>
   * 제네릭을 사용해서 클래스 내부에서 사용할 데이터 타입을 클래스가 인스턴스화될 때 지정
   * 재사용성: 다양한 타입에 대해 재사용 가능한 클래스를 작성할 수 있다.
   * 타입 안정성: 컴파일 타임에 타입 검사를 통해 런타임 오류를 줄일 수 있다
   * 가독성: 코드가 더 명확해지고 읽기 쉬워진다. 
   */
  @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
        private int size;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}    
```
- 엔티티를 `DTO`로 변환해서 반환한다.
- 엔티티가 변해도 API 스펙이 변경되지 않는다.
- 추가로 `Result` 클래스로 컬렉션을 감싸서 향후 필요한 필드를 추가할 수 있다.
  - ex) `size`

더 간단하게 적용하기 
```java
@GetMapping("/api/v2/members")
public Result membersV2(){
    
    List<MemberDto> collect = memberService.findMembers()
            .stream()
            .map(m -> new MemberDto(m.getName()))
            .collect(Collectors.toList());
    
    return new Result(collect, collect.size());
}
```
