# 순수 JPA 페이징과 정렬

- JPA에서 페이징을 어떻게 할 것인가?
- 다음 조건으로 페이징과 정렬을 사용하는 예제 코드
  - 검색 조건: 나이가 10살
  - 정렬 조건: 이름으로 내림차순
  - 페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 3건

### JPA 페이징 리포지토리 코드

```java
public List<Member> findByPage(int age, int offset, int limit){
    return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member.class)
            .setParameter("age", age)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
}
public Long totalCount(int age){
    return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
            .setParameter("age", age)
            .getSingleResult();
}
```

### JPA 페이징 테스트 코드

```java
@Test
public void paging(){
    // given
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member1", 10));

    int age = 10;
    int offset = 0;
    int limit = 3;

    // when
    List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
    Long totalCount = memberJpaRepository.totalCount(age);

    // then
    assertThat(members.size()).isEqualTo(3);
    assertThat(totalCount).isEqualTo(5);
}
```