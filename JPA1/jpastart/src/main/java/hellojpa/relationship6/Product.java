package hellojpa.relationship6;

import jakarta.persistence.*;
import java.util.*;
// @Entity
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    @ManyToMany(mappedBy = "products")
    private List<Member> members = new ArrayList<>();

}
