# View 환경 설정

### thymeleaf 템플릿 엔진

- [thymeleaf 공식 사이트](https://www.thymeleaf.org/)
- [스프링 공식 튜토리얼](https://spring.io/guides/gs/serving-web-content/)
- [스프링부트 메뉴얼](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-template-engines)

### index.html

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
 <title>Hello</title>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
Hello
<a href="/hello">hello</a>
</body>
</html>
```

### 타임리프 테스트 

hello.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <p th:text="${data}"> 안녕하세요! </p>
</body>
</html>
```

HelloController
```java
@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data", "hello!!!");
        return "hello";
    }
}
```

### 참고 - spring-boot-devtools

build.gradle - 추가
- `implementation 'org.springframework.boot:spring-boot-devtools'`
- `html` 파일을 컴파일만 해주면 서버 재시작 없이 `View` 파일 변경이 가능하다.
- 인텔리제이 컴파일 방법: 메뉴 `build` ➡️ `Recompile`


