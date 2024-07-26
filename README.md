# 인프런 스프링 부트와 JPA 실무 완전 정복 로드맵 공부 💪

## 자바 ORM 표준 JPA 프로그래밍 - 기본편
- [Section2 JPA 시작하기](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpastart/Section2%20JPA%20%EC%8B%9C%EC%9E%91%ED%95%98%EA%B8%B0)
  - H2DB 설정, DB 방언(dialect), EntityManagerFactory, EntityManager
- [Section3 영속성 - 내부 동작 방식](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpastart/Section3%20%EC%98%81%EC%86%8D%EC%84%B1%20-%20%EB%82%B4%EB%B6%80%20%EB%8F%99%EC%9E%91%20%EB%B0%A9%EC%8B%9D)
  - 영속성 컨텍스트 상태(영속, 준영속), 영속성 컨텍스트 기능, 1차 캐시, 스냅샷, 플러시
- Section4 엔티티 매핑
  - [4-(1~4) 엔티티 테이블 매핑](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpastart/Section4%20%EC%97%94%ED%8B%B0%ED%8B%B0%20%EB%A7%A4%ED%95%91)
    - 객체와 테이블 매핑, 하이버네이트 DDL AUTO, 필드와 컬럼 매핑, 기본키 매핑 전략(IDENTITY, SEQUENCE, TABLE)
  - [4-5 실전 예제, 기본 매핑](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpashop/Section4%20%EC%97%94%ED%8B%B0%ED%8B%B0%20%EB%A7%A4%ED%95%91)
    - 기본 매핑, 데이터 중심 설계의 문제점
- [Section5 연관관계 매핑 기초](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpastart/Section5%20%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%20%EB%A7%A4%ED%95%91%20%EA%B8%B0%EC%B4%88)
    - ``@OneToMany, @ManyToOne, @JoinColumn``, mappedBy, 관계의 주인, 객체 모델링과 테이블 모델링 패러다임 일치, 단방향, 양방향 
- Section6 다양한 연관관계 매핑 
  - [다양한 연관관계 매핑 이론, 연습](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpastart/Section6%20%EB%8B%A4%EC%96%91%ED%95%9C%20%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%20%EB%A7%A4%ED%95%91)
    - 1:1, 1:N, N:1, N:M
  - [매핑 실전예제3](https://github.com/jeus1998/SpringJPA/blob/main/JPA1/jpashop/Section6%20%EB%8B%A4%EC%96%91%ED%95%9C%20%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%20%EB%A7%A4%ED%95%91/6-5%20%EC%8B%A4%EC%A0%84%20%EC%98%88%EC%A0%9C3%20-%20%EB%8B%A4%EC%96%91%ED%95%9C%20%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%20%EB%A7%A4%ED%95%91.md)
    - 1:1 관계, N:M 관계, 계층형 데이터(카테고리)
- [Section7 고급매핑](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpastart/Section7%20%EA%B3%A0%EA%B8%89%20%EB%A7%A4%ED%95%91)
  - `@Inheritance`, 3가지 전략: `JOINED, SINGLE_TABLE, TABLE_PER_CLASS`, `@DiscriminatorColumn`, `@DiscriminatorValue`
  - `@MappedSuperclass`, 매핑 정보 상속, 베이스 엔티티 
## 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발

## 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화

## 스프링 데이터 JPA

## Querydsl
