package jpabook.jpashop.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
public class Category extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;
    private String name;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_ID")  // 계층형 엔티티 - 카테고리 (양방향)
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "CATEGORY_ITEM",   // N:M 양방향 - 중간 테이블 매핑
            joinColumns = @JoinColumn(name = "CATEGORY_ID") ,
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID")
    )
    private List<Item> items = new ArrayList<>();
}
