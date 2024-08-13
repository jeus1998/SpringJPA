# Web 확장 - 페이징과 정렬

- 스프링 데이터가 제공하는 페이징과 정렬 기능을 스프링 MVC에서 편리하게 사용할 수 있다.

### 페이징과 정렬 예제

```java
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    /**
     * pageRequest 객체가 넘어온다
     * 쿼리스트링으로 값 넣는게 가능 page & size & sort
     * ex) http://localhost:8080/members?page=0&size=3&sort=id,desc
     */
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
        return memberRepository.findAll(pageable);
    }

    @PostConstruct
    public void init(){
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
```
- 파라미터로 `Pageable`을 받을 수 있다. 
- `Pageable`은 인터페이스, 실제는 `org.springframework.data.domain.PageRequest`객체 생성

### 요청 파라미터

- 예) `/members?page=0&size=3&sort=id,desc&sort=username,desc`
- page: 현재 페이지, 0부터 시작한다.
- size: 한 페이지에 노출할 데이터 건수
- sort: 정렬 조건을 정의한다.
  - 정렬 속성,정렬 속성...(ASC | DESC), 정렬 방향을 변경하고 싶으면 sort 파라미터 추가 (asc 생략 가능)

### 기본값

글로벌 설정: 스프링 부트 yaml or properties
```yaml
spring:
  data:
    web:
      pageable:
        default-page-size: 10
        max-page-size: 2000
```

개별 설정 - `@PageableDefault` 애노테이션 사용 
```java
@GetMapping("/members")
public Page<Member> list(@PageableDefault(size = 12, sort = "username", direction = Sort.Direction.DESC) Pageable pageable){
    return memberRepository.findAll(pageable);
}
```

접두사 
- 페이징 정보가 둘 이상이면 접두사로 구분
- `@Qualifier`에 접두사명 추가 `"{접두사명}_xxx"`
- 예제: /members?member_page=0&order_page=1

```java
public String list(
 @Qualifier("member") Pageable memberPageable,
 @Qualifier("order") Pageable orderPageable, ...
```

### Page 내용을 DTO로 변환하기

- 엔티티를 API로 노출하면 다양한 문제가 발생한다. 그래서 엔티티를 꼭 DTO로 변환해서 반환해야 한다.
- Page는 map()을 지원해서 내부 데이터를 다른 것으로 변경할 수 있다.


MemberDto
```java
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
}
```

@GetMapping("/members") - Page.map() 사용
```java
@GetMapping("/members")
public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
    return memberRepository.findAll(pageable).map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTeam().getName()));
}
```

### Page.map() 코드 최적화

MemberDto
```java
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
```

@GetMapping("/members2") - Page.map() 코드 최적화
```java
@GetMapping("/members2")
public Page<MemberDto> list2(Pageable pageable){
    return memberRepository.findAll(pageable)
            .map(MemberDto::new);
}
```

### Page를 1부터 시작하기

- 스프링 데이터는 Page를 0부터 시작한다.
- 만약 1부터 시작하려면?

2가지 방법 
- 직접 클래스 만들어서 처리 
  - Pageable, Page를 파리미터와 응답 값으로 사용히지 않고, 직접 클래스를 만들어서 처리
  - 직접 PageRequest(Pageable 구현체)를 생성해서 리포지토리에 넘긴다.
  - 물론 응답값도 Page 대신에 직접 만들어서 제공해야 한다.
- `spring.data.web.pageable.one-indexed-parameters`를 true 로 설정
  - 그런데 이 방법은 web에서 page 파라미터를 -1 처리 할 뿐이다. 
  - 따라서 응답값인 Page 에 모두 0 페이지 인덱스를 사용하는 한계가 있다.