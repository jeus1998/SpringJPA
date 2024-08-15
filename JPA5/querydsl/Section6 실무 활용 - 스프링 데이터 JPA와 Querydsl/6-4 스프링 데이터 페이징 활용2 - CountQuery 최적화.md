# 스프링 데이터 페이징 활용2 - CountQuery 최적화

```java
// 카운트 최적화 
JPAQuery<Member> countQuery = queryFactory
        .select(member)
        .from(member)
        .leftJoin(member.team, team)
        .where(usernameEq(condition.getUsername()),
                teamNameEq(condition.getTeamName()),
                ageGoe(condition.getAgeGoe()),
                ageLoe(condition.getAgeLoe()));

return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
```
- `PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);`
- 스프링 데이터 라이브러리가 제공
- count 쿼리가 생략 가능한 경우 생략해서 처리
  - 페이지가 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때 
    - 전체 데이터(9), offset(0), limit(10) ➡️ content(9) ➡️ 카운트 쿼리 생략 전체 카운트: 9
  - 마지막 페이지, 컨텐츠 사이즈가 페이지 사이즈보다 작을 때 
    - 전체 데이터(103), offset(100), limit(10) ➡️ content(3) ➡️ 카운트 쿼리 생략 전체 카운트: 103