package jpql.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.*;
@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    public List<Member> getMembers() {
        return members;
    }
    public void setMembers(List<Member> members) {
        this.members = members;
    }
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
