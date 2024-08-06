# 회원 수정 API

### 회원 수정 API - 추가 

```java
/**
 * CQS 패턴 적용 Command Query Separate
 */
@PutMapping("/api/v2/members/{id}")
public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

    memberService.update(id, request.getName());    // Member 반환 X
    Member findMember = memberService.findOne(id);  // 응답을 위한 데이터 다시 조회
    return new UpdateMemberResponse(findMember.getId(), findMember.getName());
}
@Data
static class UpdateMemberRequest{
    private String name;
}

@Data
@AllArgsConstructor
static class UpdateMemberResponse{
    private Long id;
    private String name;
}

// MemberService 
    
/**
* Dirty Checking - 변경 감지 활용한 데이터 변경
*/
@Transactional
public void update(Long id, String name) {
   Member findMember = memberRepository.findOne(id);
   findMember.setName(name);
}


```
- 회원 수정도 `DTO`를 요청 파라미터에 매핑
- 변경 감지를 사용해서 데이터를 수정
- 회원 수정 `API updateMemberV2` 은 회원 정보를 부분 업데이트 한다. 여기서 `PUT` 방식을 사용했는데, 
  `PUT`은 전체 업데이트를 할 때 사용하는 것이 맞다. 부분 업데이트를 하려면 `PATCH`를 사용하거나 `POST`를
  사용하는 것이 `REST` 스타일에 맞다.

### CQS 패턴 

- https://velog.io/@yena1025/CQS-Command-Query-Separation

