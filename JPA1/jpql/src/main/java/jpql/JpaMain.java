package jpql;

import jakarta.persistence.*;
import jpql.domain.Address;
import jpql.domain.Member;
import jpql.domain.Order;
import jpql.domain.Team;
import jpql.dto.MemberDto;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member();
            member.setUsername("zeus");
            member.setAge(100);
            em.persist(member);

            MemberDto singleResult =
                    em.createQuery("select new jpql.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                            .getSingleResult();

            System.out.println("username = " + singleResult.getUsername());
            System.out.println("age = " + singleResult.getAge());

            tx.commit();
        }
        catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
        }
        emf.close();
    }
}
