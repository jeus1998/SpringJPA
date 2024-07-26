package hellojpa.proxy;

import hellojpa.relationship7.Member;
import hellojpa.relationship7.Team;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {
            Member member = new Member();
            member.setUsername("hello");

            em.persist(member);

            em.flush();
            em.clear();
            Member refMember = em.getReference(Member.class, member.getId());
            System.out.println("findMember = " + refMember.getClass());

            Hibernate.initialize(refMember); // 프록시 강제 초기화

            System.out.println("refMember = " + emf.getPersistenceUnitUtil().isLoaded(refMember)); // true


            tx.commit();
        }
        catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
            emf.close();
        }
    }

    private static void printMember(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team);
    }
}
