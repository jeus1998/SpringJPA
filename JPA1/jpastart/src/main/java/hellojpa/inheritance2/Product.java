package hellojpa.inheritance2;

import jakarta.persistence.*;

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
