# 회원 등록 API

### 회원 등록 API - V1

/api/MemberApiController
```java
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse{
        private Long id;
    }
}
```
- 등록 V1: 요청 값으로 `Member` 엔티티를 직접 받는다.
- 문제점
  - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다. 
  - 엔티티에 `API` 검증을 위한 로직이 들어간다. (@NotEmpty 등등)
  - 실무에서는 회원 엔티티를 위한 `API`가 다양하게 만들어지는데, 한 엔티티에 각각의 `API`를 위한
    모든 요청 요구사항을 담기는 어렵다.
    - ex) 간편 가입, 여러가지 소셜을 통한 회원가입(구글, 카카오, 네이버 등..)
  - 엔티티가 변경되면 `API` 스펙이 변한다.
- 결론
  - `API` 요청 스펙에 맞추어 별도의 `DTO`를 파라미터로 받는다.
  - V2에 적용 

### 회원 등록 API - V2

- V2 엔티티 대신에 `DTO`를 `RequestBody`에 매핑

```java
@PostMapping("/api/v2/members")
public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
    Member member = new Member();
    member.setName(request.getName());
    return new CreateMemberResponse(memberService.join(member));
}

/**
 * DTO - Data Transfer Object
 * 간단히 이름만 저장하는 회원가입 API 스펙
 * Validation: @NotEmpty name 적용
 */
@Data
static class CreateMemberRequest{
    @NotEmpty
    private String name;
}

@Data
@AllArgsConstructor
static class CreateMemberResponse{
    private Long id;
}
```
- `CreateMemberRequest` 를 Member 엔티티 대신에 `RequestBody`와 매핑한다.
- 엔티티와 프레젠테이션 계층을 위한 로직을 분리할 수 있다.
- 엔티티와 API 스펙을 명확하게 분리할 수 있다.
- 엔티티가 변해도 API 스펙이 변하지 않는다.
- 참고: 실무에서는 엔티티를 API 스펙에 노출하면 안된다!




