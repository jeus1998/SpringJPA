# JPQL - 엔티티 직접 사용

### 엔티티 직접 사용 - 기본 키 값

- `JPQL`에서 엔티티를 직접 사용하면 `SQL`에서 해당 엔티티의 기본 키 값을 사용

```sql
[JPQL]
select count(m.id) from Member m //엔티티의 아이디를 사용
select count(m) from Member m //엔티티를 직접 사용

[SQL](JPQL 둘다 같은 다음 SQL 실행)
select count(m.id) as cnt from Member m                                
```

엔티티를 파라미터로 전달
```java
Member findMember = em.createQuery("select m from Member m where m = :member", Member.class)
        .setParameter("member", member1)
        .getSingleResult();
System.out.println("findMember = " + findMember);

실행 결과
Hibernate: 
    /* select
        m 
    from
        Member m 
    where
        m = :member */ select
            m1_0.MEMBER_ID,
            m1_0.age,
            m1_0.memberType,
            m1_0.TEAM_ID,
            m1_0.username 
        from
            Member m1_0 
        where
            m1_0.MEMBER_ID=?
findMember = Member{id=1, username='회원1', age=0}
```

식별자를 직접 전달
```java
Member findMember = em.createQuery("select m from Member m where m.id = :memberId", Member.class)
        .setParameter("memberId", member1.getId())
        .getSingleResult();
System.out.println("findMember = " + findMember);

실행 결과
Hibernate: 
    /* select
        m 
    from
        Member m 
    where
        m = :member */ select
            m1_0.MEMBER_ID,
            m1_0.age,
            m1_0.memberType,
            m1_0.TEAM_ID,
            m1_0.username 
        from
            Member m1_0 
        where
            m1_0.MEMBER_ID=?
findMember = Member{id=1, username='회원1', age=0}
```
- 엔티티를 직접 사용한 결과와 동일하다. 
- 즉 엔티티를 직접 사용하면 엔티티의 식별자를 사용해서 조회한다!!

### 엔티티 직접 사용 - 외래 키 값

```java
Member findMember = em.createQuery("select m from Member m where m.team = :team", Member.class)
        .setParameter("team", teamB)
        .getSingleResult();
System.out.println("findMember = " + findMember);

Hibernate: 
    /* select
        m 
    from
        Member m 
    where
        m.team = :team */ select
            m1_0.MEMBER_ID,
            m1_0.age,
            m1_0.memberType,
            m1_0.TEAM_ID,
            m1_0.username 
        from
            Member m1_0 
        where
            m1_0.TEAM_ID=?
findMember = Member{id=3, username='회원3', age=0}
```