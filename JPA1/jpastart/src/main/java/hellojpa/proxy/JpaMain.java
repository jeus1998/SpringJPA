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
            //저장
           Team team = new Team();
           team.setName("TeamA");

           em.persist(team);

           Member member1 = new Member();
           Member member2 = new Member();
           member1.setUsername("member1");
           member1.setTeam(team);
           member2.setUsername("member2");
           member2.setTeam(team);

           em.persist(member1);
           em.persist(member2);

           em.flush();
           em.clear();


            System.out.println("=======================================");
            Team findTeam = em.createQuery("SELECT t FROM Team t WHERE t.id = :id", Team.class)
                             .setParameter("id", team.getId())
                             .getSingleResult();
            System.out.println("=======================================");
            System.out.println(findTeam.getMembers().getClass());
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(findTeam.getMembers()));
            List<Member> members = findTeam.getMembers();
            for (Member m : members) {
                System.out.println(emf.getPersistenceUnitUtil().isLoaded(findTeam.getMembers()));
                System.out.println("m.getClass() = " + m.getClass());
                System.out.println("m.getUsername() = " + m.getUsername());
            }

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
