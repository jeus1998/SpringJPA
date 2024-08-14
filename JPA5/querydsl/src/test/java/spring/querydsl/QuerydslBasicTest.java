package spring.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import spring.querydsl.entity.Member;
import spring.querydsl.entity.QMember;
import spring.querydsl.entity.QTeam;
import spring.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.querydsl.entity.QMember.*;
import static spring.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;
    @BeforeEach
    public void before(){
       queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();
    }

    @Test
    public void startJPQL(){
        // member1을 찾아라
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl(){
        QMember m = new QMember("m");

        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1")) // 파라미터 바인딩 처리
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void staticQtype(){
        // import static spring.querydsl.entity.QMember.*;
        Member findMember = queryFactory
                .select(member) // static import 상태
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
    @Test
    public void searchAndParam(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"),
                        (member.age.eq(10)))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch(){
        // 리스트 반환 없으면 빈 리스트
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();
        // 결과가 없으면 null 결과가 둘 이상이면 com.querydsl.core.NonUniqueResultException
        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();


        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();// limit(1).fetchOne();

        // 페이징 정보 포함, total count 쿼리 추가 실행
        // total count 쿼리가 무거워지면 사용 x 분리 해야함
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        long total = results.getTotal();
        List<Member> content = results.getResults();

        // count 쿼리로 변경해서 count 수 조회
        long totalCount = queryFactory
                .selectFrom(member)
                .fetchCount();
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    public void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }

    @Test
    public void paging1(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) // 0부터 시작 (zero index) -> 정렬된 테이블 결과에서 row 행 시작 위치를 말함
                .limit(2)  // 최대 2건 조회
                .fetch();

        System.out.println("result = " + result);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUsername()).isEqualTo("member3");
        assertThat(result.get(1).getUsername()).isEqualTo("member2");
    }

    @Test
    public void paging2(){
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        // count 쿼리를 통해 전체 조회 수
        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().get(0).getUsername()).isEqualTo("member3");
        assertThat(queryResults.getResults().get(1).getUsername()).isEqualTo("member2");
    }

    @Test
    public void aggregation(){
        Tuple result = queryFactory
                .select(member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetchOne();
        assertThat(result.get(member.count())).isEqualTo(4);
        assertThat(result.get(member.age.sum())).isEqualTo(100);
        assertThat(result.get(member.age.avg())).isEqualTo(25);
        assertThat(result.get(member.age.max())).isEqualTo(40);
        assertThat(result.get(member.age.min())).isEqualTo(10);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     */
    @Test
    // @Commit
    public void group(){
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg().as("평균연령"))
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .orderBy(team.name.asc())
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg().as("평균연령"))).isEqualTo(15); // (10 + 20) / 2

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg().as("평균연령"))).isEqualTo(35); // (30 + 40) / 2
    }

    /**
     * 팀 A에 소속된 모든 회원을 찾아라
     */
    @Test
    public void join(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    /**
     * 세타 조인
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    public void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member);
        }

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: select m, t from Member m left join m.team t on t.name = 'teamA'
     */
    @Test
    public void join_on_filtering(){
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 연관관계가 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    public void join_on_no_relation(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
                .on(team.name.eq(member.username))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void fetchJoinNo(){
        em.flush();
        em.clear();

        List<Member> members = queryFactory
                .selectFrom(member)
                .fetch();
        // N+1
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println(Persistence.getPersistenceUtil().isLoaded(member.getTeam())); // false
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }

    @Test
    public void fetchJoinYes(){
        em.flush();
        em.clear();

        List<Member> members = queryFactory
                .select(member)
                .from(member)
                .join(member.team, team).fetchJoin()
                .fetch();
        // 1개의 쿼리
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println(Persistence.getPersistenceUtil().isLoaded(member.getTeam())); // true
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }

}
