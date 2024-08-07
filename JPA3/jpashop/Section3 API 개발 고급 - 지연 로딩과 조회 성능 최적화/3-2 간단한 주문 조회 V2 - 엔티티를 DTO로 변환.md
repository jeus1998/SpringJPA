# 간단한 주문 조회 V2 - 엔티티를 DTO로 변환

### OrderSimpleApiController - V2 추가

```java
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
@Getter
static class Result<T>{
    private int orderCount;
    private T data;
    public Result(T data, int count){
        this.data = data;
        this.orderCount = count;
    }
}
@Getter
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
- 엔티티를 `DTO`로 변환하는 일반적인 방법이다.
- 쿼리가 총 1 + N + N번 실행된다. (v1과 쿼리수 결과는 같다.)
  - `order` 조회 1번(order 조회 결과 수가 N이 된다.)
  - `order` ➡️ `member` 지연 로딩 조회 N 번
  - `order` ➡️ `delivery` 지연 로딩 조회 N 번
  - 예) `order`의 결과가 4개면 최악의 경우 1 + 4 + 4번 실행된다.(최악의 경우)
    - 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략한다.

참고 
```text
현재 스프링 부트 3.0 이상은 하이버네이트 6.0 버전을 사용하는데 
1:1 관계 매핑 최적화에 문제가 있다. 

v2를 포스트맨에서 조회하면 Delivery 관련 쿼리가 N번 더 날라간다. 
```
- [인프런 질문](https://www.inflearn.com/community/questions/823816/%EC%BF%BC%EB%A6%AC%EA%B0%80-%EA%B0%95%EC%9D%98%EB%B3%B4%EB%8B%A4-%EB%8D%94-%EB%82%98%EC%98%A4%EA%B3%A0-%EC%9E%88%EC%8A%B5%EB%8B%88%EB%8B%A4)
