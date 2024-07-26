package hellojpa;

import hellojpa.inheritance.Item;
import hellojpa.inheritance.Movie;
import jakarta.persistence.*;

import java.util.List;

/**
 * JPA 구동 방식
 * 1. Persistence: 설정 정보 조회 (METE-INF/persistence.xml)
 * 2. Persistence: EntityManagerFactory 생성
 * 3. EntityManagerFactory: EntityManager 생성
 * JPA는 항상 모든 동작을 트랜잭션 단위로 시작을 한다.
 * EntityManager 생성 동작은 커넥션 획득
 */

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
            Movie movie = new Movie();
            movie.setDirector("aaa");
            movie.setActor("bbb");
            movie.setName("괴물");
            movie.setPrice(10000);

            em.persist(movie);
            em.flush();
            em.clear();

            // 부모 타입을 조회
            Item item = em.find(Item.class, movie.getId());
            System.out.println("item = " + item);

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
