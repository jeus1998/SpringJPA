package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final OrderQueryRepository orderQueryRepository;

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

    /**
     * V2 엔티티 -> Dto 변환해서 반환
     * 엔티티를 절대 노출시키지 말자
     */
    @GetMapping("/api/v2/orders")
    public Result ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        return new Result(orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList()));
    }

    /**
     * V3- Fetch Join 사용
     */
    @GetMapping("/api/v3/orders")
    public Result ordersV3(){
        return new Result(orderRepository.findAllWithItem()
                .stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList()));
    }

    /**
     * V3-1 Fetch Join + 페이징
     */
    @GetMapping("/api/v3-1/orders")
    public Result ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ){
        // 해당 쿼리를 toOne 관계를 패치 조인해서 가져온 결과 페이징에 영향 X
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        return new Result( orders
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList())
        );
    }
    @GetMapping("/api/v4/orders")
    public Result ordersV4(){
        return new Result(orderQueryRepository.findOrderQueryDtos());
    }
    @Getter
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
    @Getter
    @AllArgsConstructor
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems()
                    .stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }
    /**
     * API 에서 요구사항:  상품명, 상품 주문 가격, 주문수량 3가지
     */
    @Getter
    static class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
