package jpql;

import jakarta.persistence.*;
import jpql.domain.Address;
import jpql.domain.Member;
import jpql.domain.Order;
import jpql.domain.Team;
import jpql.dto.MemberDto;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team team = new Team();
            team.setName("A");
            em.persist(team);

            Member member = new Member();
            member.setUsername("A");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("A2");
            member2.setAge(20);
            member2.changeTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            List<Order> resultList = em.createQuery("select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p )").getResultList();
            System.out.println("size = " + resultList.size());

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
