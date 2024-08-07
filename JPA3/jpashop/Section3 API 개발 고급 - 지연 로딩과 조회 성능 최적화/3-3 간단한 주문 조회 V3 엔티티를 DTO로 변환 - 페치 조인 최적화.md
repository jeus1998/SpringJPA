# 간단한 주문 조회 V3 - 엔티티를 DTO로 변환 - 페치 조인 최적화

### OrderSimpleApiController - V3 추가

```java
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
```

### OrderRepository - 추가 코드(findAllWithMemberDelivery)

```java
public List<Order> findAllWithMemberDelivery() {
    return em.createQuery("select o from Order o join fetch o.member m join fetch o.delivery d",
            Order.class).getResultList();
}
```
- 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회
- 페치 조인으로 `order -> member`, `order -> delivery` 는 이미 조회 된 상태 이므로 지연로딩 ❌


```text
 select
        o1_0.order_id,
        d1_0.delivery_id,
        d1_0.city,
        d1_0.street,
        d1_0.zipcode,
        d1_0.status,
        m1_0.member_id,
        m1_0.city,
        m1_0.street,
        m1_0.zipcode,
        m1_0.name,
        o1_0.order_date,
        o1_0.status 
    from
        orders o1_0 
    join
        member m1_0 
            on m1_0.member_id=o1_0.member_id 
    join
        delivery d1_0 
            on d1_0.delivery_id=o1_0.delivery_id
```