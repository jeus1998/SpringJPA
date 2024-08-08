# 주문 조회 V6 - JPA에서 DTO로 직접 조회, 플랫 데이터 최적화

### OrderQueryDto

```java
@Getter
@Setter
@EqualsAndHashCode(of = "orderId")
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;
    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, List<OrderItemQueryDto> orderItems) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderItems = orderItems;
    }
}
```
- stream()연산에서 Collection `equals & hashcode`를 위해서 롬복 사용 
- 생성자 추가 

### OrderItemQueryDto

```java
@Getter
@Setter
public class OrderItemQueryDto {
    @JsonIgnore
    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int orderCount;
    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int orderCount) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
    }
}
```

### OrderFlatDto

```java
@Getter
@Setter
public class OrderFlatDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private String itemName;
    private int orderPrice;
    private int orderCount;
    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int orderCount) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
    }
}
```

### OrderQueryRepository 추가

```java
public List<OrderFlatDto> findAllByDto_flat() {
return em.createQuery("select new " +
                " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
        " from Order o" +
        " join o.member m" +
        " join o.delivery d" +
        " join o.orderItems oi" +
        " join oi.item i", OrderFlatDto.class)
        .getResultList();
}
```
- 조인으로 전체 데이터 가져오기 
- 당연히 페이징 사용 불가 

### OrderApiController에 V6 - 추가

```java
@GetMapping("/api/v6/orders")
    public Result ordersV6(){
        List<OrderFlatDto> flat = orderQueryRepository.findAllByDto_flat();

        // grouping & mapping
        List<OrderQueryDto> collect = flat.stream()
                .collect(
                        // 키
                        Collectors.groupingBy(f -> new OrderQueryDto(f.getOrderId(), f.getName(), f.getOrderDate(), f.getOrderStatus(), f.getAddress()),
                        // 값
                        Collectors.mapping(f -> new OrderItemQueryDto(f.getOrderId(), f.getItemName(), f.getOrderPrice(), f.getOrderCount()), Collectors.toList())))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue())).collect(Collectors.toList());

        // toMap 활용
        List<OrderQueryDto> collect1 = flat.stream()
                .collect(Collectors.toMap(
                        f -> f.getOrderId(), // 키 설정
                        f -> { // 값 설정
                            List<OrderItemQueryDto> items = new ArrayList<>();
                            items.add(new OrderItemQueryDto(f.getOrderId(), f.getItemName(), f.getOrderPrice(), f.getOrderCount()));
                            return new OrderQueryDto(f.getOrderId(), f.getName(), f.getOrderDate(), f.getOrderStatus(), f.getAddress(), items);
                        }, // 병합 함수
                        (existing, replacement) -> {
                            existing.getOrderItems().addAll(replacement.getOrderItems());
                            return existing;
                        }
                )).values().stream().collect(Collectors.toList());
        return new Result(collect1);

    }
```
- 2가지 방법 
  - grouping & mapping
  - toMap
- Query: 1번
- 단점
  - 쿼리는 한번이지만 조인으로 인해 DB에서 애플리케이션에 전달하는 데이터에 중복 데이터가 추가되므로
    상황에 따라 V5 보다 더 느릴 수 도 있다.
  - 애플리케이션에서 추가 작업이 크다.
  - 페이징 불가능

