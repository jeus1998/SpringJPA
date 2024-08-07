package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * xToOne
 * Order
 * Order -> Member    - ManyToOne
 * Order -> Delivery  - OneToOne
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Member 강제 초기화
            order.getDelivery().getStatus(); // Delivery 강제 초기화
            // OrderItem 초기화 X
        }
        return all;
    }

    /**
     * 엔티티를 조회해서 DTO로 변환
     * 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2(){
        List<SimpleOrderDto> collect = orderRepository.findAllByString(new OrderSearch())
                .stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return new Result(collect, collect.size());
    }

    /**
     * 엔티티를 조회해서 DTO로 변환
     * fetch join 사용
     * fetch join으로 쿼리 1번 호출
     */
    @GetMapping("/api/v3/simple-orders")
    public Result ordersV3(){
        return new Result(orderRepository.findAllWithMemberDelivery()
                .stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList()));
    }

    @Getter
    static class Result<T>{
        private int orderCount;
        private T data;
        public Result(T data) {
            this.data = data;
        }
        public Result(T data, int count){
            this.data = data;
            this.orderCount = count;
        }
    }
    @Getter
    @Slf4j
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
