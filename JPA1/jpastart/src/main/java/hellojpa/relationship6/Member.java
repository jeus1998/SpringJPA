package hellojpa.relationship6;

import jakarta.persistence.*;
import java.util.*;
// @Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT") // 중간테이블 이름
    private List<Product> products = new ArrayList<>();

}
