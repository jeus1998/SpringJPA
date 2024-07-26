package jpabook.jpashop.domain;

import jakarta.persistence.*;

import static jakarta.persistence.FetchType.*;

@Entity
public class Delivery extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;
    @OneToOne(fetch = LAZY ,mappedBy = "delivery") // 1대1 - 단방향 매핑
    private Order order;
    private String city;        // city, street, zipcode : 배송지 주소
    private String street;
    private String zipcode;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
