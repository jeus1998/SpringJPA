package hellojpa.persistencecontext;

import hellojpa.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * 영속성 컨텍스트 특징: 엔티티 조회, 1차 캐시
 * 영속성 컨텍스트 1차 캐시에 엔티티가 있으면 캐시에서 반환 없으면 데이터베이스 조회 쿼리를 날린다.
 * 영속성 컨텍스트 안에 MAP 형태로 KEY: PK /  VALUE: Entity
 */
public class Cache {
    public static void main(String[] args) {

        // 엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 획득
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 비영속
            Member member = new Member();
            // member.setId(100L);
            // member.setName("1차 cache");
            // 영속(1차 캐시에 저장)
            em.persist(member);

            // 영속성 컨텍스트 내부 1차 캐시에서 조회 즉 데이터베이스로 쿼리 전송 x
            Member findMember = em.find(Member.class, 100L);

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
}
