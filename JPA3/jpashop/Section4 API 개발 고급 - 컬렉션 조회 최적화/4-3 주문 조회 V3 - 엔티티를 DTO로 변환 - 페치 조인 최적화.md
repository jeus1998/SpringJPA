# 주문 조회 V3 - 엔티티를 DTO로 변환 - 페치 조인 최적화

### OrderApiController V3 - 추가 

```java
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
```

### OrderRepository - findAllWithItem() 추가 

```java
public List<Order> findAllWithItem() {
    return em.createQuery(
            "select o From Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery d " +
                    "join fetch o.orderItems oi " +
                    "join fetch oi.item i", Order.class)
            .getResultList();
}
```

### 정리 

- 페치 조인으로 `SQL`이 1번만 실행됨
- 하이버네이트 6.0 이전 버전은 Distinct 사용해야함 

h2 데이터베이스에 실행된 쿼리 직접 사용하기 
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
        oi1_0.order_id,
        oi1_0.order_item_id,
        oi1_0.count,
        i1_0.item_id,
        i1_0.dtype,
        i1_0.name,
        i1_0.price,
        i1_0.stock_quantity,
        i1_0.artist,
        i1_0.etc,
        i1_0.author,
        i1_0.isbn,
        i1_0.actor,
        i1_0.director,
        oi1_0.order_price,
        o1_0.status 
    from
        orders o1_0 
    join
        member m1_0 
            on m1_0.member_id=o1_0.member_id 
    join
        delivery d1_0 
            on d1_0.delivery_id=o1_0.delivery_id 
    join
        order_item oi1_0 
            on o1_0.order_id=oi1_0.order_id 
    join
        item i1_0 
            on i1_0.item_id=oi1_0.item_id
```

![1.png](Image%2F1.png)

- `Order`는 2개인데 `OrderItem`과 조인을 해서 데이터가 2배 증가 
- `distinct` 를 사용한 이유는 1대다 조인이 있으므로 데이터베이스 `row`가 증가한다.
- 그 결과 같은 `order` 엔티티의 조회 수도 증가하게 된다. 
- `JPA`의 `distinct`는 `SQL`에 `distinct`를 추가하고, 더해서 같은 엔티티가 조회되면, 애플리케이션에서 중복을 걸러준다.
- 이 예에서 `order`가 컬렉션 페치 조인 때문에 중복 조회 되는 것을 막아준다.
- 단점
  - 페이징 불가능
- 하이버네이트 6.0 이후 버전에서는 컬렉션 패치 조인을 하면 자동으로 `distinct`를 추가한다.
- 스프링 부트 3.0 - 하이버네이트 6.0 사용 

참고
```text
컬렉션 페치 조인을 사용하면 페이징이 불가능하다. 
하이버네이트는 경고 로그를 남기면서 모든 데이터를 DB에서 읽어오고, 메모리에서 페이징 해버린다(매우 위험하다)
 
컬렉션 페치 조인은 1개만 사용할 수 있다. 컬렉션 둘 이상에 페치 조인을 사용하면 안된다. 데이터가 부정합하게 조회될 수 있다.
```