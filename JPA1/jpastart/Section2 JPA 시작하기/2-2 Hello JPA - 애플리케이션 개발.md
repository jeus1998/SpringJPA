# Hello JPA - 애플리케이션 개발

### JPA 동작 

JPA 구동 방식 
![5.png](Image%2F5.png)

JPA 동작 확인 - JpaMain
```java
/**
 * JPA 구동 방식
 * 1. Persistence: 설정 정보 조회 (METE-INF/persistence.xml)
 * 2. Persistence: EntityManagerFactory 생성
 * 3. EntityManagerFactory: EntityManager 생성
 */

public class JpaMain {

    public static void main(String[] args) {

        // EntityManagerFactory 생성 (persistenceUnitName 필요)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // EntityManger 생성
        EntityManager em = emf.createEntityManager();

        // 항상 어떤 데이터베이스에 쿼리를 날릴 때(트랜잭션 단위) EntityManager 생성한다.
        em.close();

        // 애플리케이션 종료시점에 EntityManagerFactory close
        emf.close();
    }
}
```
### 데이터베이스 테이블 생성 
```sql
create table member(
      id bigint not null,
      name varchar(255),
      primary key (id)
);
```

### Member Entity 생성

```java
@Entity
public class Member {
    @Id
    private Long id;
    private String name;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
```

### 간단한 회원 저장 , 주의점 

```java
/**
 * JPA 구동 방식
 * 1. Persistence: 설정 정보 조회 (METE-INF/persistence.xml)
 * 2. Persistence: EntityManagerFactory 생성
 * 3. EntityManagerFactory: EntityManager 생성
 * JPA는 항상 모든 동작을 트랜잭션 단위로 시작을 한다.
 * EntityManager 생성 동작은 커넥션 획득
 */

public class JpaMain {

    public static void main(String[] args) {

        // EntityManagerFactory 생성 (persistenceUnitName 필요)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // EntityManger 생성 => 항상 어떤 데이터베이스에 쿼리를 날릴 때(트랜잭션 단위) EntityManager 생성한다.
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 획득, 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 멤버 저장 로직
        Member member = new Member();
        member.setId(1L);
        member.setName("HelloA");
        em.persist(member);

        tx.commit();

        em.close();

        // 애플리케이션 종료시점에 EntityManagerFactory close
        emf.close();
    }
}
```
- 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
- 엔티티 매니저는 쓰레드간에 공유X (사용하고 버려야 한다). 
- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

### 회원 저장 정석 로직 

```java
public class JpaMain {

    public static void main(String[] args) {

        // EntityManagerFactory 생성 (persistenceUnitName 필요)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // EntityManger 생성 => 항상 어떤 데이터베이스에 쿼리를 날릴 때(트랜잭션 단위) EntityManager 생성한다.
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 획득, 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 멤버 저장 로직
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");
            em.persist(member);

            tx.commit();
        }
        catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
        }

        // 애플리케이션 종료시점에 EntityManagerFactory close
        emf.close();
    }
}
```

### 회원 (SELECT, UPDATE) 

SELECT
```java
try {
    // 멤버 SELECT 로직
    Member findMember = em.find(Member.class, 1L);
    System.out.println("findMember.id = " + findMember.getId());
    System.out.println("findMember.name = " + findMember.getName());

    tx.commit();
}
catch (Exception e){
    tx.rollback();
}
finally {
    em.close();
}
```

UPDATE
```java
try {
    // 멤버 UPDATE 로직
    Member findMember = em.find(Member.class, 1L);
    findMember.setName("HelloJPA");

    tx.commit();
}
catch (Exception e){
    tx.rollback();
}
finally {
    em.close();
}
```

### JPQL 소개 

가장 단순한 조회 방법
- ``EntityManager.find()``
- 나이가 18살 이상인 회원을 모두 검색하고 싶다면?
- JPQL

JPQL?
- ``JPA``를 사용하면 엔티티 객체를 중심으로 개발
- 문제는 검색 쿼리
- 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
- 모든 ``DB`` 데이터를 객체로 변환해서 검색하는 것은 불가능
- 애플리케이션이 필요한 데이터만 ``DB``에서 불러오려면 결국 검색 조건이 포함된 ``SQL``이 필요
- ``JPA``는 ``SQL``을 추상화한 ``JPQL``이라는 객체 지향 쿼리 언어 제공
- ``SQL``과 문법 유사, ``SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN``지원
- ``JPQL``은 엔티티 객체를 대상으로 쿼리
- ``SQL``은 데이터베이스 테이블을 대상으로 쿼리
- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
- ``SQL``을 추상화해서 특정 데이터베이스 ``SQL``에 의존X 
- ``JPQL``을 한마디로 정의하면 객체 지향 ``SQL``

### JPQL 사용 

```java
try {
// 멤버 전제 SELECT
List<Member> members = em.createQuery("select m from Member as m", Member.class)
        .getResultList();

for (Member member : members) {
    System.out.println("member.id = " + member.getId());
    System.out.println("member.name = " + member.getName());
}

tx.commit();
}
```
실행 결과
```text
member.id = 1
member.name = HelloJPA
member.id = 2
member.name = HelloB
```
