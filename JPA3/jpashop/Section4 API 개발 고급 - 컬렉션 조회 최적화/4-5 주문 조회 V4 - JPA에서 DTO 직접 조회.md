# 주문 조회 V4 - JPA에서 DTO 직접 조회

### OrderQueryDto 

```java
@Getter
@Setter
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
}
```

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

### OrderQueryRepository

```java
/**
 * OrderRepository - 엔티티 반환
 * OrderQueryRepository - 화면(API)에 의존이 있는 리포지토리 (화면과 관련)
 * 관심사 분리
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();
        result.forEach(o ->{
            o.setOrderItems(findOrderItems(o.getOrderId()));
        });
        return result;
    }
    /**
     * 1: N 관계 가져오기 + N:1(OrderItem, Item) 조인해서 가져오기
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i " +
                " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
    /**
     * 먼저 N:1 , 1:1 조인으로 가져오기
     */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
```

### OrderApiController에 V4 - 추가

```java
@GetMapping("/api/v4/orders")
public Result ordersV4(){
    return new Result(orderQueryRepository.findOrderQueryDtos());
}
```

### 정리 

- Query: 루트 1번, 컬렉션 N번 실행
  - N+1 문제 발생 
- ToOne(N:1, 1:1) 관계들을 먼저 조회하고, ToMany(1:N) 관계는 각각 별도로 처리한다.
  - 이런 방식을 선택한 이유는 다음과 같다
  - `ToOne` 관계는 조인해도 데이터 `row` 수가 증가하지 않는다.
  - ToMany(1:N) 관계는 조인하면 `row` 수가 증가한다.
- `row` 수가 증가하지 않는 `ToOne` 관계는 조인으로 최적화 하기 쉬우므로 한번에 조회하고, `ToMany` 관계는 최적화 하기 
  어려우므로 findOrderItems()같은 별도의 메서드로 조회한다.
