# Querydsl 지원 클래스 직접 만들기

- 스프링 데이터가 제공하는 `QuerydslRepositorySupport`가 지닌 한계를 극복하기 위해 직접 Querydsl 지원 클래스를 만들어보자.
- 장점 
  - 스프링 데이터가 제공하는 페이징을 편리하게 변환
  - 페이징과 카운트 쿼리 분리 가능
  - 스프링 데이터 Sort 지원
  - select() , selectFrom() 으로 시작 가능
  - `EntityManager` , `QueryFactory` 제공

### Querydsl4RepositorySupport

```java
/**
 * Querydsl 4.x 버전에 맞춘 Querydsl 지원 라이브러리
 *
 * @author Younghan Kim
 * @see org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
 */
@Repository
public abstract class Querydsl4RepositorySupport {
    private final Class domainClass;
    private Querydsl querydsl;
     private EntityManager entityManager;
    private JPAQueryFactory queryFactory;
    public Querydsl4RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        this.domainClass = domainClass;
    }
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        JpaEntityInformation entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath path = resolver.createPath(entityInformation.getJavaType());
        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }
    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }
    protected Querydsl getQuerydsl() {
        return querydsl;
    }
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }
    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getQueryFactory().selectFrom(from);
    }
    protected <T> Page<T> applyPagination(Pageable pageable, Function<JPAQueryFactory, JPAQuery> contentQuery) {
        JPAQuery jpaQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaQuery).fetch();
        return PageableExecutionUtils.getPage(content, pageable, jpaQuery::fetchCount);
    }
    protected <T> Page<T> applyPagination(Pageable pageable, Function<JPAQueryFactory, JPAQuery> contentQuery, Function<JPAQueryFactory, JPAQuery> countQuery) {
        JPAQuery jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaContentQuery).fetch();
        JPAQuery countResult = countQuery.apply(getQueryFactory());
        return PageableExecutionUtils.getPage(content, pageable, countResult::fetchCount);
    }
}
```

### Querydsl4RepositorySupport 사용 코드

```java
@Repository
public class MemberTestRepository extends Querydsl4RepositorySupport {
    public MemberTestRepository() {
        super(Member.class);
    }
    public List<Member> basicSelect(){
        return select(member)
                .from(member)
                .fetch();
    }
    public List<Member> basicSelectFrom() {
        return selectFrom(member)
                .fetch();
    }
    public Page<Member> searchPageByApplyPage(MemberSearchCondition condition, Pageable pageable){
        JPAQuery<Member> query = selectFrom(member)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()));

        List<Member> content = getQuerydsl().applyPagination(pageable, query).fetch();
        return PageableExecutionUtils.getPage(content, pageable, query::fetchCount);
    }

    public Page<Member> applyPagination(MemberSearchCondition condition, Pageable pageable){
        return applyPagination(pageable, query ->
                query.selectFrom(member)
                        .where(usernameEq(condition.getUsername()),
                                teamNameEq(condition.getTeamName()),
                                ageGoe(condition.getAgeGoe()),
                                ageLoe(condition.getAgeLoe())));
    }
    public Page<Member> applyPagination2(MemberSearchCondition condition, Pageable pageable){
        return applyPagination(pageable, query ->
                query.selectFrom(member)
                        .where(usernameEq(condition.getUsername()),
                                teamNameEq(condition.getTeamName()),
                                ageGoe(condition.getAgeGoe()),
                                ageLoe(condition.getAgeLoe())),
                countQuery -> countQuery
                        .select(member.id)
                        .from(member)
                        .leftJoin(member.team, team)
                        .where(usernameEq(condition.getUsername()),
                                teamNameEq(condition.getTeamName()),
                                ageGoe(condition.getAgeGoe()),
                                ageLoe(condition.getAgeLoe())));
    }
    private BooleanExpression ageGoe(Integer ageGoe) {
           return ageGoe == null ? null : member.age.goe(ageGoe);
       }
    private BooleanExpression ageLoe(Integer ageLoe) {
       return ageLoe == null ? null : member.age.loe(ageLoe);
   }
    private BooleanExpression teamNameEq(String teamName) {
       return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }
    private BooleanExpression usernameEq(String username) {
       return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }
}
```