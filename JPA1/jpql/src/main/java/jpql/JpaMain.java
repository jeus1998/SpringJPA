package jpql;

import jakarta.persistence.*;
import jpql.domain.*;
import jpql.dto.MemberDto;
import jpql.test.Book;
import jpql.test.Item;
import jpql.test.Movie;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static jpql.domain.MemberType.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Team team = new Team();
            team.setName("B");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            em.flush();
            em.clear();

            List<String> resultList = em.createQuery("select NULLIF(m.username, 'member1') as username from Member m", String.class).getResultList();
            for (String s : resultList) {
                System.out.println(s);
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
