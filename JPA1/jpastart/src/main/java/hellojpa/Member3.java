package hellojpa;

import jakarta.persistence.*;

/**
 * 기본 키 매핑 공부용
 * 시퀀스 테스트를 위한 Member3
 * 각각의 엔티티마다 고유한 시퀀스가 존재한다.  Member3_SEQ
 */
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER3_SEQ", // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 1) // initialValue default = 1 allocationSize default = 50
public class Member3 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "name")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
