# 조건식 - CASE 식

### 기본 CASE 식

jpql
```sql
select
     case when m.age <= 10 then '학생요금'
          when m.age >= 60 then '경로요금'
          else '일반요금'
     end
from Member m
```


```java
Member member = new Member();
member.setUsername("member1");
member.changeTeam(team);
member.setAge(5);
em.persist(member);

em.flush();
em.clear();

String query =
        "select " +
        "CASE " +
        "WHEN m.age <= 10 then '학생요금'" +
        "when m.age >= 60 then '경로요금'" +
        "else '일반요금' " +
        "end " +
        "FROM Member m";

List<String> resultList = em.createQuery(query, String.class).getResultList();
for (String s : resultList) {
    System.out.println(s);
}

실행 결과
Hibernate: 
    /* select
        CASE 
            WHEN m.age <= 10 
                then '학생요금'
            when m.age >= 60 
                then '경로요금'
            else '일반요금' 
        end 
    FROM
        Member m */ select
            case 
                when m1_0.age<=10 
                    then '학생요금' 
                when m1_0.age>=60 
                    then '경로요금' 
                else '일반요금' 
            end 
        from
            Member m1_0
학생요금
```
- 5살 이니까 학생요금으로 잘 나왔다. 

### 단순 CASE 식

jpql
```sql
select
     case t.name 
         when '팀A' then '인센티브110%'
         when '팀B' then '인센티브120%'
         else '인센티브105%'
     end
from Team t
```


```java
Team team = new Team();
team.setName("B");
em.persist(team);

Member member = new Member();
member.setUsername("member1");
member.changeTeam(team);
member.setAge(5);
em.persist(member);

em.flush();
em.clear();

String query =
        "select " +
        "case t.name " +
        "when 'A' then 'zz'" +
        "when 'B' then 'zzz'" +
        "end " +
        "from Team t";

List<String> resultList = em.createQuery(query, String.class).getResultList();
for (String s : resultList) {
    System.out.println(s);
}

실행 결과 

Hibernate: 
    /* select
        case t.name 
            when 'A' 
                then 'zz'
            when 'B' 
                then 'zzz'
            end 
        from
            Team t */ select
                case t1_0.name 
                    when 'A' 
                        then 'zz' 
                    when 'B' 
                        then 'zzz' 
                    end 
                from
                    Team t1_0
zzz
```

### 조건식 - CASE 식

- `COALESCE`: 하나씩 조회해서 `null`이 아니면 반환
- `NULLIF`: 두 값이 같으면 `null` 반환, 다르면 첫번째 값 반환

COALESCE 테스트 
```java
Member member = new Member();
member.setUsername("member1");
em.persist(member);

Member member2 = new Member();
em.persist(member2);

em.flush();
em.clear();

List<String> resultList = em.createQuery("select coalesce(m.username, '이름 없는 회원') as username from Member m", String.class).getResultList();
for (String s : resultList) {
    System.out.println(s);
}

실행 결과

member1
이름 없는 회원
```
- JPQL: `em.createQuery("select coalesce(m.username, '이름 없는 회원') as username from Member m", String.class)`
- 이름을 넣은 member1은 `member1` 반환 
- 이름을 넣지 않은 member2는 `'이름 없는 회원'` 반환

NULLIF 테스트
```java
Member member = new Member();
member.setUsername("member1");
em.persist(member);

Member member2 = new Member();
member2.setUsername("member2");
em.persist(member2);

em.flush();
em.clear();

List<String> resultList = em.createQuery("select NULLIF(m.username, 'member1') as username from Member m", String.class).getResultList();
for (String s : resultList) {
    System.out.println(s);
}

실행 결과 

null
member2
```
- JPQL: `em.createQuery("select NULLIF(m.username, 'member1') as username from Member m", String.class)`
- 두 값이 같으면 `null` 반환, 다르면 첫번째 값 반환
- `member1`은 `member1`과 같아서 `null` 반환 
- `member2`는 `member1`과 달라서 `member2` 반환


