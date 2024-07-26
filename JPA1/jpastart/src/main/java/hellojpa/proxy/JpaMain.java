package hellojpa.proxy;

import hellojpa.relationship7.Member;
import hellojpa.relationship7.Team;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {
            Team team = new Team();
            team.setName("레알 마드리드");

            Team team2 = new Team();
            team2.setName("바르셀로나");

            em.persist(team);
            em.persist(team2);

            Member member = new Member();
            member.setUsername("hello");
            member.setTeam(team);

            Member member2 = new Member();
            member2.setUsername("hello");
            member2.setTeam(team2);

            Member member3 = new Member();
            member3.setUsername("hello");
            member3.setTeam(team2);

            em.persist(member);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

            // Member m = em.find(Member.class, member.getId());

            List<Member> members = em.createQuery("select m from Member as m join fetch m.team", Member.class)
                    .getResultList();


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
