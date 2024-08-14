# 스프링 부트 설정 - JPA, DB

### H2 DB

- H2DB 생성: `jdbc:h2:~/querydsl`
- H2DB 접속: `jdbc:h2:tcp://localhost/~/querydsl`

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/querydsl
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        # show_sql: true
        format_sql: true
  data:
    web:
      pageable:
        default-page-size: 10
        max-page-size: 2000

logging:
  level:
    org.hibernate.SQL: debug
    # org.hibernate.type: trace
```
- show_sql : 옵션은 System.out 에 하이버네이트 실행 SQL을 남긴다.
- org.hibernate.SQL : 옵션은 logger를 통해 하이버네이트 실행 SQL을 남긴다.

### 쿼리 파라미터 로그 남기기

- 로그에 다음을 추가하기 org.hibernate.type : SQL 실행 파라미터를 로그로 남긴다
- 외부 라이브러리 사용
  - https://github.com/gavlyukovskiy/spring-boot-data-source-decorator
- `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'`
