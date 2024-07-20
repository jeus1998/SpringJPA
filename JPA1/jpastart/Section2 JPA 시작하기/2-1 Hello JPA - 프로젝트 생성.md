# Hello - JPA 프로젝트 생성 

## H2 데이터베이스 설치와 실행

### H2 데이터베이스 소개 

- H2 데이터베이스는 개발이나 테스트 용도로 사용하기 좋은 가볍고 편리한 DB이다.
- SQL을 실행할 수 있는 웹 화면을 제공한다.
- 사이트: https://www.h2database.com

### H2 데이터베이스 다운로드 및 설치

- h2 데이터베이스는 2.2.224 버전을 다운로드 받아서 설치
  - https://github.com/h2database/h2database/releases/download/version-2.2.224/h2-2023-09-17.zip
- 다음 링크에 가면 과거 다양한 H2 버전을 확인할 수 있다.
  - https://www.h2database.com/html/download-archive.html

### H2 데이터베이스 실행 

MAC, 리눅스 사용자
- 디렉토리 이동 : cd bin
- 권한 주기: chmod 755 h2.sh
- 실행: ./h2.sh

윈도우 사용자
- 실행: h2.bat
- 커맨드로 H2 실행 실행 방법 (Windows)
  - H2가 설치된 경로로 이동: cd /H2/bin
  - h2.bat 실행: h2.bat

H2 데이터베이스를 실행하면 웹 브라우저가 열리면서 다음과 같은 화면을 확인할 수 있다.
![1.png](Image%2F1.png)

### H2 데이터베이스 파일 생성 방법

![2.png](Image%2F2.png)
- 사용자명은 ``sa``입력
- JDBC URL에 다음 입력
  - ``jdbc:h2:~/test``(최초 한번) 이 경우 ``연결 시험``을 호출하면 오류가 발생한다. ``연결``을 직접 눌러주어야 한다.
- 본인 홈 폴더로 이동한 다음 ```~/test.mv.db```파일 생성 확인(숨김 파일)
- 연결에 성공하면 반드시 화면 왼쪽 위에 있는 빨간색 연결 끊기 버튼을 선택해셔 연결을 끊어야 한다.
- ``jdbc:h2:~/test``로 연결을 유지하면 자바 애플리케이션에서 DB에 접근할 수 없다.
- ``jdbc:h2:~/test``와 같이 연결하는 것은 데이터베이스 파일을 만들기 위해 딱1번만 수행하는 것이다.

### TCP로 다시 연결하기

![3.png](Image%2F3.png)
- 데이터베이스 파일이 만들어지고 나면 반드시 이렇게 tcp를 통해서 접속해야 한다.
- ``jdbc:h2:tcp://localhost/~/test``
- 이렇게 tcp를 통해서 접근해야 자바 애플리케이션에서 DB에 접근할 수 있다.

참고
- 만약 그래도 데이터베이스에 접근할 수 없다면 다음 URL에 접근한 다음 H2 데이터베이스 접속 오류 부분을 확인해보자.
- https://bit.ly/3fX6ygx

## JPA 설정하기

### JPA 설정하기 - persistence.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2" xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value=""/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments"  value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="create" />
        </properties>
    </persistence-unit>

</persistence>
```

- JPA 설정 파일
- main/resources/META-INF/persistence.xml 위치
- persistence-unit name으로 이름 지정
- jakarta.persistence로 시작: JPA 표준 속성
- hibernate로 시작: 하이버네이트 전용 속성

### 데이터베이스 방언

- JPA는 특정 데이터베이스에 종속 X 
- 각각의 데이터베이스가 제공하는 SQL 문법과 함수는 조금씩 다름
  - 가변 문자: MySQL은 VARCHAR, Oracle은 VARCHAR2 
  - 문자열을 자르는 함수: SQL 표준은 SUBSTRING(), Oracle은 SUBSTR()
  - 페이징: MySQL은 LIMIT , Oracle은 ROWNUM 
- 방언: SQL 표준을 지키지 않는 특정 데이터베이스만의 고유한 기능

![4.png](Image%2F4.png)

데이터베이스 방언 dialect 설정
- hibernate.dialect 속성에 지정
  - H2 : org.hibernate.dialect.H2Dialect 
  - Oracle 10g : org.hibernate.dialect.Oracle10gDialect 
  - MySQL : org.hibernate.dialect.MySQL5InnoDBDialect 
- 하이버네이트는 40가지 이상의 데이터베이스 방언 지원