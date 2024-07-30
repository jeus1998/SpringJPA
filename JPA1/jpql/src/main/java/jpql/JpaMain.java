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
            Team team1 = new Team();
            team1.setName("A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("B");
            em.persist(team2);

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team2);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.changeTeam(team2);
            em.persist(member2);

            em.flush();
            em.clear();

            List<String> resultList = em.createQuery("select m.username From Team t join t.members m", String.class)
                    .getResultList();

            System.out.println(resultList);

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
