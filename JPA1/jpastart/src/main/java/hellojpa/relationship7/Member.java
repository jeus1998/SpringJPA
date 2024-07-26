package hellojpa.relationship7;

import hellojpa.inheritance.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// @Entity
public class Member  extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
