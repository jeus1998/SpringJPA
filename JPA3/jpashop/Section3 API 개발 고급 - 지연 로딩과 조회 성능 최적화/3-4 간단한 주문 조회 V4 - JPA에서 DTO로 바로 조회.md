# 3-4 간단한 주문 조회 V4 - JPA에서 DTO로 바로 조회

### OrderSimpleApiController - V4 추가

```java
/**
 * JPA에서 DTO로 바로 조회
 * 쿼리 1번 호출
 * select 절에서 원하는 데이터만 선택해서 조회
 */
@GetMapping("/api/v4/simple-orders")
public Result ordersV4(){
    return new Result(orderRepository.findOrderDtos()
            .stream()
            .collect(Collectors.toList()));
}
```

### DTO - OrderSimpleQueryDto

```java
@Getter
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```


### OrderRepository - 추가(findOrderDtos())

```java
public List<OrderSimpleQueryDto> findOrderDtos() {
return em.createQuery(
        "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                "from Order o" +
        " join o.member m" +
        " join o.delivery d", OrderSimpleQueryDto.class)
        .getResultList();
}
```
- DTO 직접 조회


### 실행 결과 

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
```
- 일반적인 `SQL`을 사용할 때 처럼 원하는 값을 선택해서 조회
- `new` 명령어를 사용해서 `JPQL`의 결과를 `DTO`로 즉시 변환
- SELECT 절에서 원하는 데이터를 직접 선택하므로 ➡️ DB 애플리케이션 네트웍 용량 최적화(생각보다 미비)
  - 생각보다 성능차이가 거의 없다. 
  - 하지만 데이터가 많고 고객 트래픽이 많다고 하면 고려하자 
- 리포지토리 재사용성 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점

### 정리

- 엔티티를 `DTO`로 변환하거나, `DTO`로 바로 조회하는 두가지 방법은 각각 장단점이 있다.
- 둘중 상황에 따라서 더 나은 방법을 선택하면 된다.
- 엔티티로 조회하면 리포지토리 재사용성도 좋고, 개발도 단순해진다.

쿼리 방식 선택 권장 순서
1. 우선 엔티티를 `DTO`로 변환하는 방법을 선택한다. (V3)
2. 필요하면 페치 조인으로 성능을 최적화 한다. ➡️ 대부분의 성능 이슈가 해결된다. (V3)
3. 그래도 안되면 `DTO`로 직접 조회하는 방법을 사용한다 (V4) - 성능 최적화용 리포지토리를 생성 
4. 최후의 방법은 `JPA`가 제공하는 `네이티브 SQL`이나 스프링 `JDBC Template`을 사용해서 `SQL`을 직접 사용한다.


