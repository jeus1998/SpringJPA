package hellojpa.relationship3;

import jakarta.persistence.*;

import java.util.*;

/**
 * 단방향 연관관계에서 양방향 연결
 * 사실 데이터베이스에서는 방향 개념이 없다 외래키 하나로 조인을 하면된다.
 * 하지만 객체에선 방향이 존재한다.
 */
@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    @OneToMany(mappedBy = "team") // Member에서 Team 참조 변수
    private List<Member> members = new ArrayList<>();
    private String name;

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
