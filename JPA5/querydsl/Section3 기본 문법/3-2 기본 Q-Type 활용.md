# 기본 Q-Type 활용

### Q클래스 인스턴스를 사용하는 2가지 방법

```java
QMember qMember = new QMember("m"); // 별칭 직접 지정
QMember qMember = QMember.member; //기본 인스턴스 사용
```

### 기본 인스턴스를 static import와 함께 사용

```java
import static study.querydsl.entity.QMember.*;

@Test
public void staticQtype(){
    Member findMember = queryFactory
            .select(member) // static import 상태
            .from(member)
            .where(member.username.eq("member1"))
            .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");
}
```
- 같은 테이블을 조인해야 하는 경우가 아니면 기본 인스턴스를 사용하자 

### JPQL 로그 설정 

```yaml
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/querydsl
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    properties:
      hibernate:
        use_sql_comments: true # JPQL 
```