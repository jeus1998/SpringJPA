package spring.data_jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import spring.data_jpa.entity.Member;


@Getter
@ToString(of = {"id", "username", "teamName"})
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;
    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
    // map 편의 메서드
    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.teamName = member.getTeam().getName();
    }
}
