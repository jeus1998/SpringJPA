package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.jpashop.domain.product.Book;

public class JpaMain {
    public static void main(String[] args) {
        // EntityManagerFactory 생성 (persistenceUnitName 필요)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // EntityManger 생성 => 항상 어떤 데이터베이스에 쿼리를 날릴 때(트랜잭션 단위) EntityManager 생성한다.
        EntityManager em = emf.createEntityManager();
        // 트랜잭션 획득, 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");
            em.persist(book);
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
