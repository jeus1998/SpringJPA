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
- [Section8 프록시와 연관관계 관리](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpastart/Section8%20%ED%94%84%EB%A1%9D%EC%8B%9C%EC%99%80%20%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%20%EA%B4%80%EB%A6%AC)
  - 프록시, 프록시 초기화 과정, 프록시 관련 편의 메서드, 즉시 로딩(EAGER), 지연 로딩(LAZY)
  - 영속성 전이(CASCADE), 고아 객체(orphanRemoval = true)
- [Section9 값 타입](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpastart/Section9%20%EA%B0%92%20%ED%83%80%EC%9E%85)
  - 임베디드 타입: `@Embeddable, @Embedded`, `@AttributeOverride, @AttributeOverrides`
  - 값 타입 & 불변 객체: 부작용(side effect)
  - 값 타입의 비교: 동일성 & 동등성(equals(), hashcode())
  - 값 타입 컬렉션: `@ElementCollection, @CollectionTable`, 값 타입 컬렉션의 제약사항
- [Section10 JPQL 기본 문법](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpql/Section10%20%EA%B0%9D%EC%B2%B4%EC%A7%80%ED%96%A5%20%EC%BF%BC%EB%A6%AC%20%EC%96%B8%EC%96%B41%20-%20%EA%B8%B0%EB%B3%B8%20%EB%AC%B8%EB%B2%95)
  - TypeQuery, Query, 파라미터 바인딩(이름 기준, 위치 기준)
  - 프로젝션(엔티티, 임베디드, 스칼라 타입), 여러 값 조회(Query 타입, Object[] 타입, new 명령어)
  - 페이징 API - setFirstResult(), setMaxResults
  - 조인, 서브 쿼리, JPQL 타입 표현
  - 조건식 - CASE, 기본 함수, 사용자 정의 함수 
- [Section11 JPQL 중급 문법](https://github.com/jeus1998/SpringJPA/tree/main/JPA1/jpql/Section11%20%EA%B0%9D%EC%B2%B4%EC%A7%80%ED%96%A5%20%EC%BF%BC%EB%A6%AC%20%EC%96%B8%EC%96%B42%20-%20%EC%A4%91%EA%B8%89%20%EB%AC%B8%EB%B2%95)
  - 경로 표현식: 상태 필드, 연관 필드, 단일 값 연관 경로, 컬렉션 연관 경로 
  - 페치 조인: 페치 조인 특징 & 한계, `@BatchSize()`, N+1 문제, 컬렉션 패치 조인과 페이징 문제 
  - 다형성 쿼리(TYPE(), TREAT()), 엔티티 직접 사용(엔티티 식별자(pk))
  - 네임드 쿼리: `@NamedQuery`, createNamedQuery()
  - 벌크 연산: executeUpdate(), 벌크 연산 주의점(영속성 컨텍스트), Dirty Checking vs 벌크 연산  
## 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발

- [Section1 프로젝트 환경 설정](https://github.com/jeus1998/SpringJPA/tree/main/JPA2/jpashop/Section1%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%ED%99%98%EA%B2%BD%EC%84%A4%EC%A0%95)
  - 라이브러리 체크, View 환경 설정(타임리프), spring-boot-devtools - 서버 재시작❌ View 파일 변경
  - H2 데이터베이스 설치 
  - JPA 설정 & DB 설정 - Yaml, Build.gradle
- [Section2 도메인 분석, 설계](https://github.com/jeus1998/SpringJPA/tree/main/JPA2/jpashop/Section2%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%EB%B6%84%EC%84%9D%20%EC%84%A4%EA%B3%84)
  - 요구사항 분석, 도메인 모델과 테이블 설계, 엔티티 클래스 개발 
  - 엔티티 설계시 주의점(연관관계 편의 메서드, @Setter, Cascade, 즉시로딩(EAGER), 컬렉션 필드 초기화)
- [Section3 애플리케이션 구현 준비](https://github.com/jeus1998/SpringJPA/tree/main/JPA2/jpashop/Section3%20%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%20%EA%B5%AC%ED%98%84%20%EC%A4%80%EB%B9%84)
  - 구현 요구사항, 애플리케이션 아키텍처 설명 
- [Section4 회원 도메인 개발](https://github.com/jeus1998/SpringJPA/tree/main/JPA2/jpashop/Section4%20%ED%9A%8C%EC%9B%90%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%EA%B0%9C%EB%B0%9C)
  - 회원 리포지토리, 회원 서비스 개발 
  - 회원 기능 테스트 
- [Section5 상품 도메인 개발](https://github.com/jeus1998/SpringJPA/tree/main/JPA2/jpashop/Section5%20%EC%83%81%ED%92%88%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%EA%B0%9C%EB%B0%9C)
  - 상품 엔티티(비즈니스 로직 추가): 재고 추가, 재고 감소 
  - 상품 (리포지토리, 서비스) 개발 
- [Section6 주문 도메인 개발](https://github.com/jeus1998/SpringJPA/tree/main/JPA2/jpashop/Section6%20%EC%A3%BC%EB%AC%B4%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%EA%B0%9C%EB%B0%9C)
  - 주문, 주문 상품(엔티티, 리포지토리, 서비스) 개발 
  - 연관관계 편의 메서드 + 정적 펙토리 메서드(객체 생성 feat 생성자, @Builder)
  - `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
  - 도메인 모델 패턴 vs 트랜잭션 스크립트 패턴 
  - 주문 기능 테스트 
  - 동적 쿼리(순수 JPQL, JPA 표준 스펙 - Criteria)
- [Section7 웹 계층 개발](https://github.com/jeus1998/SpringJPA/tree/main/JPA2/jpashop/Section7%20%EC%9B%B9%20%EA%B3%84%EC%B8%B5%20%EA%B0%9C%EB%B0%9C)
  - 타임리프 레이아웃 아키텍처 Include-style layouts vs Hierarchical-style layouts
  - bootstrap(JS, CSS) 적용 
  - 도메인 기능 구현 
  - ⭐️ 변경 감지 vs 병합(merge)
## 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화

- [Section1 API 개발 기본](https://github.com/jeus1998/SpringJPA/tree/main/JPA3/jpashop/Section1%20API%20%EA%B0%9C%EB%B0%9C%20%EA%B8%B0%EB%B3%B8)
  - DTO 사용으로 엔티티와 프레젠테이션 계층을 분리 
  - response : 스트림 연산 & 제네릭 활용 
  - 회원 등록, 수정, 조회 API 개발 
  - CQS 패턴 - Command - Query - Separation
- [Section2 API 개발 고급 - 준비](https://github.com/jeus1998/SpringJPA/tree/main/JPA3/jpashop/Section2%20API%20%EA%B0%9C%EB%B0%9C%20%EA%B3%A0%EA%B8%89%20-%20%EC%A4%80%EB%B9%84)
  - 조회용 샘플 데이터 입력 
- [Section3 API 개발 고급 - 지연 로딩과 조회 성능 최적화](https://github.com/jeus1998/SpringJPA/tree/main/JPA3/jpashop/Section3%20API%20%EA%B0%9C%EB%B0%9C%20%EA%B3%A0%EA%B8%89%20-%20%EC%A7%80%EC%97%B0%20%EB%A1%9C%EB%94%A9%EA%B3%BC%20%EC%A1%B0%ED%9A%8C%20%EC%84%B1%EB%8A%A5%20%EC%B5%9C%EC%A0%81%ED%99%94)
  - 엔티티를 직접 노출(V1): N+1 문제, 화면 계층과 엔티티 강한 결합 문제, 양방향 연관관계 무한 순환 문제(@JsonIgnore), Json 프록시 객체 변환 문제(Hibernate5JakartaModule) 
  - 엔티티를 DTO로 변환(V2): N+1 문제 
  - 엔티티를 DTO로 변환 + 패치 조인 활용(V3): N+1 문제 해결 
  - JPA에서 DTO로 바로 조회(V4): N+1 문제 해결 + V3 보다 성능 개선, 재사용성이 떨어지는 문제, 트레이드 오프 & 권장하는 스타일  
- [Section4 API 개발 고급 - 컬렉션 조회 최적화](https://github.com/jeus1998/SpringJPA/tree/main/JPA3/jpashop/Section4%20API%20%EA%B0%9C%EB%B0%9C%20%EA%B3%A0%EA%B8%89%20-%20%EC%BB%AC%EB%A0%89%EC%85%98%20%EC%A1%B0%ED%9A%8C%20%EC%B5%9C%EC%A0%81%ED%99%94)
  - 엔티티를 직접 노출(V1)
  - 엔티티를 `DTO`로 변환(V2)
  - 엔티티를 `DTO`로 변환 + 패치 조인 활용(V3): N+1 문제 해결 
  - 엔티티를 `DTO`로 변환 + 패치 조인 활용(V3-1): N+1 문제 해결 + 페이징 문제 해결: `@BatchSize`, `hibernate.default_batch_fetch_size`
  - `JPA`에서 `DTO`로 바로 조회(V4): N+1 문제 
  - `JPA`에서 `DTO`로 바로 조회(V5): 컬렉션 조회 최적화 IN 절을 활용해서 메모리에 미리 조회해서 최적화 
  - `JPA`에서 `DTO`로 바로 조회(V6): JOIN 결과를 그대로 조회 후 애플리케이션에서 원하는 모양으로 직접 변환
    - stream 연산 : (groupingBy, mapping), toMap() + 병합합수 
- [Section5 API 개발 고급 - 실무 필수 최적화](https://github.com/jeus1998/SpringJPA/tree/main/JPA3/jpashop/Section5%20API%20%EA%B0%9C%EB%B0%9C%20%EA%B3%A0%EA%B8%89%20-%20%EC%8B%A4%EB%AC%B4%20%ED%95%84%EC%88%98%20%EC%B5%9C%EC%A0%81%ED%99%94)
  - OSIV - Open Session In View, 성능 최적화
## 스프링 데이터 JPA

- [Section1 프로젝트 환경설정](https://github.com/jeus1998/SpringJPA/tree/main/JPA4/data-jpa/Section1%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%ED%99%98%EA%B2%BD%EC%84%A4%EC%A0%95)
  - 쿼리 파라미터 로그 남기기(외부 라이브러리), application.yaml 설정, 라이브러리 체크 
- [Section2 예제 도메인 모델](https://github.com/jeus1998/SpringJPA/tree/main/JPA4/data-jpa/Section2%20%EC%98%88%EC%A0%9C%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%EB%AA%A8%EB%8D%B8)
  - JPA 복습(연관관계 편의 메서드, 지연 로딩, 롬복)
- [Section3 공동 인터페이스 기능](https://github.com/jeus1998/SpringJPA/tree/main/JPA4/data-jpa/Section3%20%EA%B3%B5%ED%86%B5%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%20%EA%B8%B0%EB%8A%A5)
  - JPA 리포지토리 VS Spring Data Jpa 리포지토리, 공통 인터페이스 설정 & 적용, 공통 인터페이스 기능 분석  
- [Section4 쿼리 메소드 기능](https://github.com/jeus1998/SpringJPA/tree/main/JPA4/data-jpa/Section4%20%EC%BF%BC%EB%A6%AC%20%EB%A9%94%EC%86%8C%EB%93%9C%20%EA%B8%B0%EB%8A%A5)
  - 메소드 이름으로 쿼리 생성 
  - `JPA NamedQuery`
  - `@Query`, DTO 조회하기 
  - 파라미터 바인딩, 반환타입 
  - 순수 JPA 페이징과 정렬, 스프링 데이터 JPA 페이징과 정렬 
  - 벌크성 수정 쿼리 - `@Modifying(clearAutomatically = true)`
  - 페치 조인 & `@EntityGraph`
  - JPA Hint & Lock
- [Section5 확장 기능](https://github.com/jeus1998/SpringJPA/tree/main/JPA4/data-jpa/Section5%20%ED%99%95%EC%9E%A5%20%EA%B8%B0%EB%8A%A5)
  - 사용자 정의 리포지토리 
  - Auditing - `@PrePersist`, `@PreUpdate`, `@EnableJpaAuditing`, `@EntityListeners(AuditingEntityListener.class)`, `AuditorAware`,
    `@CreatedBy`, `@LastModifiedBy`, `@CreatedDate`, `@LastModifiedDate`
  - 도메인 클래스 컨버터 - HTTP 파라미터로 넘어온 엔티티의 아이디로 엔티티 객체를 찾아서 바인딩
  - 페이징과 정렬 - `Pageable`, `PageableRequest`, `@PageableDefault`
- [Section6 스프링 데이터 JPA 분석](https://github.com/jeus1998/SpringJPA/tree/main/JPA4/data-jpa/Section6%20%EC%8A%A4%ED%94%84%EB%A7%81%20%EB%8D%B0%EC%9D%B4%ED%84%B0%20JPA%20%EB%B6%84%EC%84%9D)
  - `SimpleJpaRepository` 분석, save() 메서드 로직 merge() vs persist(), `Persistable` 인터페이스 활용하기 
- [Section7 나머지 기능들](https://github.com/jeus1998/SpringJPA/tree/main/JPA4/data-jpa/Section7%20%EB%82%98%EB%A8%B8%EC%A7%80%20%EA%B8%B0%EB%8A%A5%EB%93%A4)
  - Query By Example - Probe, ExampleMatcher, Example
  - Projections - 인터페이스 기반 & 클래스 기반 , 동적 Projections, 중첩 구조 처리와 최적화 
  - 네이티브 쿼리 - 네이티브 쿼리 & DTO 반환 Projections 활용 

## Querydsl

- [Section1 프로젝트 환경설정](https://github.com/jeus1998/SpringJPA/tree/main/JPA5/querydsl/Section1%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%ED%99%98%EA%B2%BD%EC%84%A4%EC%A0%95)
- [Section2 예제 도메인 모델](https://github.com/jeus1998/SpringJPA/tree/main/JPA5/querydsl/Section2%20%EC%98%88%EC%A0%9C%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%EB%AA%A8%EB%8D%B8)
- [Section3 기본 문법](https://github.com/jeus1998/SpringJPA/tree/main/JPA5/querydsl/Section3%20%EA%B8%B0%EB%B3%B8%20%EB%AC%B8%EB%B2%95)
  - Q-Type 활용 - static import  
  - 결과 조회 - fetch(), fetchOne(), fetchFirst(), fetchResults(), fetchCount()
  - 정렬 - desc(), asc(), nullsFirst(), nullsLast()
  - 페이징 - offset(), limit(), fetchResults()
  - 조인 - 기본 조인, on절 활용, 페치 조인(fetchJoint())
  - 서브 쿼리 - JPAExpressions, Case 문 - CaseBuilder, 상수 - Expressions.constant, 문자 더하기 - concat(), stringValue()
- [Section4 중급 문법](https://github.com/jeus1998/SpringJPA/tree/main/JPA5/querydsl/Section4%20%EC%A4%91%EA%B8%89%20%EB%AC%B8%EB%B2%95)
  - 프로젝션 결과 반환 - 기본 반환, 2개 이상(tuple) 반환, DTO 조회(Projections.(xxx)), 별칭(ExpressionUtils.as), `@QueryProjection`
  - 동적 쿼리 - `BooleanBuilder`, where 다중 파라미터, `BooleanExpression`
  - 수정, 삭제 벌크 연산 
  - SQL function & querydsl 내장 function
- [Section5 순수 JPA와 Querydsl](https://github.com/jeus1998/SpringJPA/tree/main/JPA5/querydsl/Section5%20%EC%8B%A4%EB%AC%B4%20%ED%99%9C%EC%9A%A9%20-%20%EC%88%9C%EC%88%98%20JPA%EC%99%80%20Querydsl)
  - 순수 JPA 리포지토리 & Querydsl - JPAQueryFactory 동시성 문제와 스프링 
  - 동적 쿼리와 성능 최적화 조회 - BooleanBuilder & where 절 파라미터 사용 
  - 조회 API 컨트롤러 개발 - profile 
- [Section6 실무 활용 - 스프링 데이터 JPA와 Querydsl](https://github.com/jeus1998/SpringJPA/tree/main/JPA5/querydsl/Section6%20%EC%8B%A4%EB%AC%B4%20%ED%99%9C%EC%9A%A9%20-%20%EC%8A%A4%ED%94%84%EB%A7%81%20%EB%8D%B0%EC%9D%B4%ED%84%B0%20JPA%EC%99%80%20Querydsl)
  - 순수 JPA 리포지토리 ➡️ 스프링 데이터 JPA 리포지토리 변환 - 사용자 정의 리포지토리 for Querydsl
  - Querydsl 페이징 fetchResults() `PageImpl`, 카운트 쿼리 분리 fetchCount(), 카운트 쿼리 분리 최적화 `PageableExecutionUtils`
- [Section7 스프링 데이터 JPA가 제공하는 Querydsl 기능](https://github.com/jeus1998/SpringJPA/tree/main/JPA5/querydsl/Section7%20%EC%8A%A4%ED%94%84%EB%A7%81%20%EB%8D%B0%EC%9D%B4%ED%84%B0%20JPA%EA%B0%80%20%EC%A0%9C%EA%B3%B5%ED%95%98%EB%8A%94%20Querydsl%20%EA%B8%B0%EB%8A%A5)


