package hellojpa.pk;

import hellojpa.Member2;
import hellojpa.Member3;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 기본키 매핑 공부용
 * 예제를 간단하기 위해서 try-catch 생략
 */
public class PK {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Member3 member3 = new Member3();
        em.persist(member3);

        tx.commit();

        em.close();
        emf.close();
    }
}
