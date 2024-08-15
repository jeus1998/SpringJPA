# 리포지토리 지원 - QuerydslRepositorySupport

장점
- getQuerydsl().applyPagination() 스프링 데이터가 제공하는 페이징을 `Querydsl`로 편리하게 변환가능
- 단! `Sort`는 오류발생
- from()으로 시작 가능(최근에는 `QueryFactory`를 사용해서 select()로 시작하는 것이 더 명시적
- EntityManager 제공

한계
- Querydsl 3.x 버전을 대상으로 만듬
- Querydsl 4.x에 나온 `JPAQueryFactory`로 시작할 수 없음
  - `select`로 시작할 수 없음 (`from`으로 시작해야함) 
- `QueryFactory` 를 제공하지 않음
- 스프링 데이터 `Sort` 기능이 정상 동작하지 않음

