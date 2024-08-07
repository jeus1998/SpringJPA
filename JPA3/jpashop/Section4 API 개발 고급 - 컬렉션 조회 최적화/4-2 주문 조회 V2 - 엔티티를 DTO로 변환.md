# 주문 조회 V2 - 엔티티를 DTO로 변환

### 엔티티를 DTO로 변환 V2-1 

```java
@GetMapping("/api/v2/orders")
public Result ordersV2(){
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());

    return new Result(orders.stream()
            .map(o -> new OrderDto(o))
            .collect(Collectors.toList()));
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
    private List<OrderItem> orderItems;
    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        // 프록시 강제 초기화
        order.getOrderItems().stream().forEach(o -> o.getItem().getName());
        orderItems = order.getOrderItems();
    }
}
```
- 현재 `OrderDto`를 보면 내부에서 `OrderItem` 엔티티를 리스트 안에 가지고 있다.
- `OrderItem` 엔티티가 변경되면 추후에 API 스펙이 무너진다. 
- `OrderItem` -> `OrderItemDto`로 변경한다.

### ### 엔티티를 DTO로 변환 V2-2

```java
/**
 * V2 엔티티 -> Dto 변환해서 반환
 */
@GetMapping("/api/v2/orders")
public Result ordersV2(){
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());

    return new Result(orders.stream()
            .map(o -> new OrderDto(o))
            .collect(Collectors.toList()));
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
```

### 정리 

- 지연 로딩으로 너무 많은 SQL 실행
- SQL 실행 수
  - order 1번
  - member , address N번(order 조회 수 만큼)
  - orderItem N번(order 조회 수 만큼)
  - item N번(orderItem 조회 수 만큼)
- List<OrderItem>➡️ List<OrderItemDto> 
  - 엔티티 노출 ❌

