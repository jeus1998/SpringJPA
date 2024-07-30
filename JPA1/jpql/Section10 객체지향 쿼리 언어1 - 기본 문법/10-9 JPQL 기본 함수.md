# JPQL 기본 함수

### 기본 함수

- CONCAT
  - 문자열 합치기 
  - `em.createQuery("select concat(m.username, 'b') as username from Member m", String.class)`
- SUBSTRING
  - 문자열 자르기 
  - `em.createQuery("select substring(m.username, 1, 3) from Member m", String.class)`
- TRIM
  - 문자열 공백 없애기 - 문자열 사이사이 공백은 X
  - `em.createQuery("select trim(m.username) from Member m", String.class)`
  - `member.setUsername("member1     ");,  member2.setUsername("    member2");`
  - 실행결과: `member1`, `member2`
- LOWER, UPPER
  - 문자열 대문자, 소문자 변환 
  - `em.createQuery("select UPPER(m.username) from Member m", String.class)`
  - `member.setUsername("member1");`
  - 실행결과: `MEMBER1`
- LENGTH
  - 문자열 길이 
- LOCATE
  - 문자열 위치 
  - `em.createQuery("select LOCATE('be', m.username) from Member m", Integer.class)`
- ABS, SQRT, MOD
  - 숫자(절댓값), 숫자(제곱근), 숫자(나눗셈)
- SIZE, INDEX(JPA 용도)
  - SIZE: `em.createQuery("select SIZE(t.members) from Team t", Integer.class)`
    - 컬렉션 size() 반환 

### 사용자 정의 함수 호출

- 하이버네이트는 사용전 방언에 추가해야 한다.
  - 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다.
  - `select function('group_concat', i.name) from Item i`






