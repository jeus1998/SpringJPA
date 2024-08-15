# 스프링 데이터 페이징 활용1 - Querydsl 페이징 연동

### 사용자 정의 인터페이스에 페이징 2가지 추가

```java
public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
}
```

### 전체 카운트를 한번에 조회하는 단순한 방법 

```java
@Override
public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
    QueryResults<MemberTeamDto> results = queryFactory
            .select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name))
            .from(member)
            .leftJoin(member.team, team)
            .where(usernameEq(condition.getUsername()),
                    teamNameEq(condition.getTeamName()),
                    ageGoe(condition.getAgeGoe()),
                    ageLoe(condition.getAgeLoe()))
            .offset(pageable.getOffset())  // OFFSET: 어디서 부터 가져올지
            .limit(pageable.getPageSize()) // LIMIT: 행을 얼마나 가져올지
            .fetchResults();

    List<MemberTeamDto> content = results.getResults();
    long total = results.getTotal();
    return new PageImpl<>(content, pageable, total);
}
```
- `Querydsl`이 제공하는 fetchResults()를 사용하면 내용과 전체 카운트를 한번에 조회할 수 있다.(실제 쿼리는 2번 호출)
- fetchResult()는 카운트 쿼리 실행시 필요없는 `order by`는 제거한다. - 만약 있다고 하면 

### 데이터 내용과 전체 카운트를 별도로 조회하는 방법

```java
/**
 * 복잡한 페이징
 * 데이터 조회 쿼리와, 전체 카운트 쿼리를 분리
 */
@Override
public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
    // content
    List<MemberTeamDto> content = queryFactory
            .select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name))
            .from(member)
            .leftJoin(member.team, team)
            .where(usernameEq(condition.getUsername()),
                    teamNameEq(condition.getTeamName()),
                    ageGoe(condition.getAgeGoe()),
                    ageLoe(condition.getAgeLoe()))
            .offset(pageable.getOffset())  // OFFSET: 어디서 부터 가져올지
            .limit(pageable.getPageSize()) // LIMIT: 행을 얼마나 가져올지
            .fetch();

    // 카운트
    Long total = queryFactory
            .select(member)
            .from(member)
            .leftJoin(member.team, team)
            .where(usernameEq(condition.getUsername()),
                    teamNameEq(condition.getTeamName()),
                    ageGoe(condition.getAgeGoe()),
                    ageLoe(condition.getAgeLoe()))
            .fetchCount();

    return new PageImpl<>(content, pageable, total);
}
```
- 전체 카운트를 조회 하는 방법을 최적화 할 수 있으면 이렇게 분리하면 된다. 
- 예를 들어서 전체 카운트를 조회할 때 조인 쿼리를 줄일 수 있다면 상당한 효과가 있다.
- 코드를 리펙토링해서 내용 쿼리과 전체 카운트 쿼리를 읽기 좋게 분리하면 좋다.