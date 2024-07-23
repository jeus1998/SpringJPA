package hellojpa.relationship2;

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
           Team team = new Team();
           team.setName("TeamA");
           em.persist(team);

           Member member = new Member();
           member.setUsername("member1");
           member.setTeam(team); //단방향 연관관계 설정, 참조 저장
           em.persist(member);

           // 영속성 컨텍스트 1차 캐시에서 조회가 아닌 DB에서 조회하기
           em.flush();
           em.clear();

           Member findMember = em.find(Member.class, member.getId());
           // 참조를 사용해서 연관관계 조회
           Team findTeam = findMember.getTeam();

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
