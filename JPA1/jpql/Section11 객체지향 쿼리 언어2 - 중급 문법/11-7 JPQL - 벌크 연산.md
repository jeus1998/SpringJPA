# JPQL - 벌크 연산

### 벌크 연산 배경 

- 재고가 10개 미만인 모든 상품의 가격을 10% 상승하려면?
- JPA 변경 감지 기능(Dirty Checking)으로 실행하려면 너무 많은 SQL 실행
  - 재고가 10개 미만인 상품을 리스트로 조회한다.
  - 상품 엔티티의 가격을 10% 증가한다.
  - 트랜잭션 커밋 시점에 변경감지가 동작한다.
- 변경된 데이터가 100건이라면 100번의 `UPDATE SQL` 각각 실행

### 벌크 연산 예제

- 쿼리 한 번으로 여러 테이블 로우 변경(엔티티)
- executeUpdate()의 결과는 영향받은 엔티티 수 반환
- `UPDATE, DELETE` 지원
- INSERT(insert into .. select, 하이버네이트 지원)

```java
int resultRows = em.createQuery("update Member m set m.age = 20")
        .executeUpdate();
System.out.println("resultRows = " + resultRows);

Hibernate: 
    /* update
        Member m 
    set
        m.age = 20 */ update Member 
    set
        age=20
resultRows = 4
```

### 벌크 연산 주의

- 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
- 2가지 방법 
  - 벌크 연산을 먼저 실행
  - 벌크 연산 수행 후 영속성 컨텍스트 초기화

정리
- 벌크 연산을 먼저 실행하거나 
- 벌크 연산을 실행한 이후 영속성 컨텍스트를 초기화 하자 
- 만약 하지 않으면 LOST UPDATE 이상 현상이 발생한다.

