package hellojpa.relationship7;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;
    @OneToMany(mappedBy = "product")
    private List<Order> orders = new ArrayList<>();
}