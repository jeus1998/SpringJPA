# 주문 조회 V5 - JPA에서 DTO 직접 조회 - 컬렉션 조회 최적화


### OrderApiController에 V5 - 추가

```java
@GetMapping("/api/v5/orders")
public Result ordersV5(){
    return new Result(orderQueryRepository.findAllByDto_optimization());
}
```

### OrderQueryRepository 추가 

```java
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;
    
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
    /**
     * 최적화
     * Query: 루트 1번, 컬렉션 1번 
     * 데이터를 한꺼번에 처리할 때 많이 사용하는 방식
     */
    public List<OrderQueryDto> findAllByDto_optimization() {

        List<OrderQueryDto> result = findOrders();

        // orderId 뽑기
        List<Long> orderIds = toOrderIds(result);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> {
            o.setOrderItems(orderItemMap.get(o.getOrderId()));
        });

        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems =
                em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi " +
                                " join oi.item i " +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                        .setParameter("orderIds", orderIds)
                        .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap =
                         orderItems
                        .stream()
                        .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result
                .stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }
}
```

로직 흐름 정리 
1. findOrders()로 toOne 연관관계(Member, Delivery) join
2. toOrderIds()로 조회한 Order `List<Long>`으로 만들기 
3. findOrderItemMap()메서드로 `where in`으로 최적화된 쿼리를 날려서 OrderItem * Item join
4. findOrderItemMap()메서드 내부에서 Collectors.groupingBy()를 사용하여 key: orderId value: OrderItemQueryDto 생성 
5. result에다가 map에서 OrderItemQueryDto 를 가져와서 set & return

쿼리 체크 
```text
select
        o1_0.order_id,
        m1_0.name,
        o1_0.order_date,
        o1_0.status,
        d1_0.city,
        d1_0.street,
        d1_0.zipcode 
    from
        orders o1_0 
    join
        member m1_0 
            on m1_0.member_id=o1_0.member_id 
    join
        delivery d1_0 
            on d1_0.delivery_id=o1_0.delivery_id
            
 select
        oi1_0.order_id,
        i1_0.name,
        oi1_0.order_price,
        oi1_0.count 
    from
        order_item oi1_0 
    join
        item i1_0 
            on i1_0.item_id=oi1_0.item_id 
    where
        oi1_0.order_id in (?, ?)                        
```

### 정리 

- Query: 루트 1번, 컬렉션 1번
- `ToOne` 관계들을 먼저 조회하고, 여기서 얻은 식별자 `orderId`로 `ToMany` 관계인 `OrderItem`을 한꺼번에 조회
- MAP을 사용해서 매칭 성능 향상(O(1))


