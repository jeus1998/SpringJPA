package spring.data_jpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.data_jpa.dto.MemberDto;
import spring.data_jpa.entity.Member;
import spring.data_jpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * 도메인 클래스 컨버터 사용
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    /**
     * pageRequest 객체가 넘어온다
     * 쿼리스트링으로 값 넣는게 가능 page & size & sort
     * ex) http://localhost:8080/members?page=0&size=3&sort=id,desc
     */
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
        return memberRepository.findAll(pageable).map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTeam().getName()));
    }

    /**
     * /members1 최적화
     */
    @GetMapping("/members2")
    public Page<MemberDto> list2(Pageable pageable){
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    // @PostConstruct
    public void init(){
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
