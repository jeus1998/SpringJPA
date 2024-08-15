package spring.querydsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import spring.querydsl.dto.MemberSearchCondition;
import spring.querydsl.entity.Member;
import spring.querydsl.entity.QMember.*;
import spring.querydsl.repository.support.Querydsl4RepositorySupport;
import java.util.List;

import static spring.querydsl.entity.QMember.*;
import static spring.querydsl.entity.QTeam.team;

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
