package hellojpa.relationship7;

import jakarta.persistence.*;

/**
 * 중간 테이블 엔티티로 승격
 */
// @Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    @Column(name = "ORDER_AMOUNT")
    private Long orderAmount; // @ManyToMany 와 다르게 중간 테이블에 컬럼 추가 가능 예시
}
