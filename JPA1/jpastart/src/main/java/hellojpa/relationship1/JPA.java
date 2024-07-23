package hellojpa.relationship1;

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

           // 영속 컨텍스트에 영속 상태가 되면 아이디(식별자)가 존재
           // SEQUENCE 는 캐시활용 / AUTO_INCREMENT 는 바로 INSERT 쿼리를 날려서 ID 저장

           // 이렇게 테이블에 맞추어 모델링하면 조회를 하면 계속 DB나 JPA에서 조회를 해야한다.
           member.setTeamId(team.getId());
           em.persist(member);

           // Member 조회
           Member findMember = em.find(Member.class, member.getId());
           Long findTeamId = findMember.getTeamId();

           // Team 식별자로 다시 조회 객체 지향적인 방법은 아니다
           Team findTeam = em.find(Team.class, findTeamId);

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
