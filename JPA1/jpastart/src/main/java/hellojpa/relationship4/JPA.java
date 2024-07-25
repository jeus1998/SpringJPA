package hellojpa.relationship4;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JPA {
    public static void main(String[] args) {

       EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

       EntityManager em = emf.createEntityManager();

       // 트랜잭션 획득, 시작
       EntityTransaction tx = em.getTransaction();
       tx.begin();

       try {

           Member member = new Member();
           member.setUsername("member1");
           em.persist(member);

           Team team = new Team();
           team.setName("teamA");
           team.getMembers().add(member);

           em.persist(team); // Member 테이블에 update 쿼리가 한번 더 날라간다.

           tx.commit();
       }
       catch (Exception e){
           tx.rollback();
       }
       finally {
           em.close();
       }

       // 애플리케이션 종료시점에 EntityManagerFactory close
       emf.close();
   }
}
