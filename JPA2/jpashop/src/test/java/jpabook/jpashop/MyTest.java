package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MyTest {
    @Autowired
    private EntityManager em;
    /**
     * 하이버네이트는 엔티티를 영속화 할 때, 컬렉션을 감싸써 하이버네이트가 제공하는 내장 컬렉션으로 변경
     * 만약 setOrders()처럼 임의의 메서드에서 컬렉션을 잘못 생성하면 하이버네이트 내부 매커니즘에 문제가 발생 할 수 있다.
     * 따라서 필드 레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다
     * 내부 메커니즘이란 해당 컬렉션 필드를 추적(Dirty checking- 변경 감지, 지연 로딩)을 제공하기 위해서
     */
    @Transactional
    @Test
    @DisplayName("컬렉션 필드 초기화")
    @Rollback(value = true)
    public void test(){
        Member member = new Member();
        // class java.util.ArrayList
        System.out.println("collection class = " + member.getOrders().getClass());
        em.persist(member);
        // class org.hibernate.collection.spi.PersistentBag
        System.out.println("collection class = " + member.getOrders().getClass());
    }
}
