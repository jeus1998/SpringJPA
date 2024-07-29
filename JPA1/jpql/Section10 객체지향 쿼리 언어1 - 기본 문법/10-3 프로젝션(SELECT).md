# 프로젝션(SELECT)

### 프로젝션?

- `SELECT` 절에 조회할 대상을 지정하는 것
- 프로젝션 대상: 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자등 기본 데이터 타입)
- `SELECT m FROM Member m` ➡️ 엔티티 프로젝션
- `SELECT m.team FROM Member m` ➡️ 엔티티 프로젝션
- `SELECT m.address FROM Member m` ➡️ 임베디드 타입 프로젝션
- `SELECT m.username, m.age FROM Member m` ➡️ 스칼라 타입 프로젝션
- `DISTINCT`로 중복 제거

### JPQL - 프로젝션

엔티티 프로젝션 
```java
List<Member> resultList = em.createQuery("select m from Member m", Member.class).getResultList();
for (Member mem : resultList) {
    System.out.println("mem = " + mem);
}
```
- `em.createQuery("select m from Member m", Member.class)`

엔티티 프로젝션2 
```java
Team team = new Team();
team.setName("바르셀로나");
em.persist(team);

Member member = new Member();
member.setUsername("member1");
member.setAge(10);
member.setTeam(team);
em.persist(member);

Team singleResult = em.createQuery("select m.team from Member m", Team.class).getSingleResult();
System.out.println("singleResult = " + singleResult);
```
- `em.createQuery("select m.team from Member m", Team.class)`
- Member & Team 조인 쿼리가 나간다(Inner Join)

임베디드 타입 프로젝션
```java
Order order = new Order();
order.setAddress(new Address("city", "street", "zipcode1"));
em.persist(order);

Address singleResult = em.createQuery("select o.address from Order as o", Address.class).getSingleResult();
System.out.println("singleResult.getCity() = " + singleResult.getCity());
```
- 실행 결과 : `singleResult.getCity() = city`

### 프로젝션 - 여러 값 조회 (여러가지 방법)

- `SELECT m.username, m.age FROM Member m`
- Query 타입으로 조회
- Object[] 타입으로 조회
- new 명령어로 조회

---

Query 타입으로 조회
```java
Query query = em.createQuery("select m.username, m.age from Member m");
Object singleResult = query.getSingleResult();
Object[] result = (Object []) singleResult;
System.out.println(result[0]);
System.out.println(result[1]);
```

---

Object[] 타입으로 조회
```java
Member member = new Member();
member.setUsername("zeus");
member.setAge(100);
em.persist(member);

List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m").getResultList();
for (Object[] objects : resultList) {
    Object [] result = (Object[]) objects;
    System.out.println("username = " + result[0]);
    System.out.println("age = " + result[1]);
}
```
---

new 명령어로 조회

MemberDto
```java
public class MemberDto {
    private String username;
    private int age;
    
    public MemberDto(String username, int age) {
       this.username = username;
       this.age = age;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}
```
- 생성자가 꼭 필요하다. 
  - JPQL 쿼리와 순서와 타입이 일치해야 함
- new 명령어로 조회에서 사용 

```java
Member member = new Member();
member.setUsername("zeus");
member.setAge(100);
em.persist(member);

MemberDto singleResult =
        em.createQuery("select new jpql.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getSingleResult();

System.out.println("username = " + singleResult.getUsername());
System.out.println("age = " + singleResult.getAge());
```
- `em.createQuery("select new jpql.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)`
- 패키지 명을 포함한 전체 클래스 명 입력
