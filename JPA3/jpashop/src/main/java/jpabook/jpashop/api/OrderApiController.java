package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * Order -> Member    - ManyToOne
 * Order -> Delivery  - OneToOne
 * Order -> OrderItem - OneToMany
 * OrderItem -> Item  - ManyToOne
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    /**
     * V1 엔티티 직접 노출
     * Hibernate5Module 모듈 등록, 기본전략  LAZY=null 처리
     * 프록시 강제 초기화를 통해 조회
     * 양방향 관계 문제 발생 -> @JsonIgnore
     * N+1 문제 발생
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();      // LAZY 강제 초기화
            order.getDelivery().getAddress(); // LAZY 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName()); // LAZY 강제 초기화
        }
        return all;
    }
}
