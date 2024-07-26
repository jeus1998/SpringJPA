# @MappedSuperclass - 매핑 정보 상속 

### @MappedSuperclass? 

![7.png](Image%2F7.png)
- 공통 매핑 정보가 필요할 때 사용(id, name)

### @MappedSuperclass 코드 

BaseEntity
```java
/**
 * @MappedSuperclass 활용
 * 공통 매핑 정보가 필요할 때 사용
 * 추상 클래스로 사용을 권장
 */
@MappedSuperclass
public abstract class BaseEntity {
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
    // getter & setter 생략 
}
```

Member -  BaseEntity
```java
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
     // getter & setter 생략 
}
```

실행 결과
```text
Hibernate: 
    create table Member (
        MEMBER_ID bigint not null,
        createdDate timestamp(6),
        lastModifiedDate timestamp(6),
        createdBy varchar(255),
        lastModifiedBy varchar(255),
        primary key (MEMBER_ID)
    )    
```
- ``BaseEntity``에서 정의한 공통 속성(컬럼)들이 ``Member``에 들어간다.


### @MappedSuperclass 특징 

- 상속관계 매핑❌
- 엔티티❌, 테이블과 매핑❌ 
- 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
- 조회, 검색 불가(em.find(BaseEntity) 불가) 
- 직접 생성해서 사용할 일이 없으므로 추상 클래스 권장
- 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
- 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
- ```@Entity```클래스는 엔티티나 ```@MappedSuperclass```로 지정한 클래스만 상속 가능

### 테스트 하기 

- ```@MappedSuperclass``` 애노테이션으로 베이스 엔티티 생성 
- 해당 베이스 엔티티를 상속받은 ``Product``엔티티 생성 
- ``Product`` 엔티티를 상속 받는 자동차, 컴퓨터 엔티티 생성 - 조인 전략 사용 

BaseEntity
```java
@MappedSuperclass
public abstract class BaseEntity {
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
```

Product
```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Product extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;
    @Column(name = "PRODUCT_NAME", nullable = false)
    private String name;
    @Column(name = "PRODUCT_PRICE", nullable = false)
    private int price;
}
```

Car
```java
@Entity
@DiscriminatorValue("Computer") // default = Computer
public class Computer extends Product{
    private String cpu;
}
```

Computer
```java
@Entity
@DiscriminatorValue("Computer") // default = Computer
public class Computer extends Product{
    private String cpu;
}
```