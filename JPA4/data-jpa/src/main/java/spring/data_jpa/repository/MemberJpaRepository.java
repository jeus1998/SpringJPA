package spring.data_jpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.data_jpa.entity.Member;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    // @PersistenceContext
    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }
    public Member find(Long id){
        return em.find(Member.class, id);
    }

}
