package jpql;

import jakarta.persistence.*;
import jpql.domain.*;
import jpql.dto.MemberDto;
import jpql.test.Book;
import jpql.test.Item;
import jpql.test.Movie;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static jpql.domain.MemberType.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);
            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);
            Team teamC = new Team();
            teamC.setName("팀C");
            em.persist(teamC);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("회원4");
            em.persist(member4);

            em.flush();
            em.clear();


            List<Team> resultList = em.createQuery("select t From Team t", Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            for (Team team : resultList) {
                System.out.println("team = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
            }

            tx.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        finally {
            em.close();
        }
        emf.close();
    }
}
