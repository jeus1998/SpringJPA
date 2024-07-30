# JPQL 타입 표현

- 문자: 'HELLO', 'She''s'
- 숫자: 10L(Long), 10D(Double), 10F(Float)
- Boolean: TRUE, FALSE
- ENUM: `jpabook.MemberType.Admin` (패키지명 포함)
- 엔티티 타입: TYPE(m) = Member (상속 관계에서 사용)

### TYPE(i) = 'Book'(상속 관계에서 사용 )

Item - `@Inheritance(strategy = InheritanceType.JOINED)`
```java
Book book = new Book();
book.setName("JPA");
book.setPrice(1000);
em.persist(book);

Movie movie = new Movie();
movie.setName("300");
movie.setPrice(100000);
em.persist(movie);

em.flush();
em.clear();

List<Item> resultList =
        em.createQuery("select i from Item i where type(i)=Book")
        .getResultList();

for (Item item : resultList) {
    System.out.println("item.getName() = " + item.getName());
    System.out.println("item.getPrice() = " + item.getPrice());
}

Hibernate: 
    /* select
        i 
    from
        Item i 
    where
        type(i)=Book */ select
            i1_0.ITEM_ID,
            i1_0.DTYPE,
            i1_0.name,
            i1_0.price,
            i1_1.author,
            i1_1.isbn,
            i1_2.actor,
            i1_2.director 
        from
            Item i1_0 
        join
            Book i1_1 
                on i1_0.ITEM_ID=i1_1.ITEM_ID 
        left join
            Movie i1_2 
                on i1_0.ITEM_ID=i1_2.ITEM_ID 
        where
            i1_0.DTYPE='Book'
item.getName() = JPA
item.getPrice() = 1000
```
- `em.createQuery("select i from Item i where type(i)=Book")`
- `Dtype`을 기준으로 찾는다. 

Item - `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`
- `strategy = InheritanceType.JOINED` 결과와 동일
- `Dtype`을 기준으로 찾는다. 


Item - `@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)`
```text
Hibernate: 
    /* select
        i 
    from
        Item i 
    where
        type(i)=Book */ select
            i1_0.ITEM_ID,
            i1_0.clazz_,
            i1_0.name,
            i1_0.price,
            i1_0.author,
            i1_0.isbn,
            i1_0.actor,
            i1_0.director 
        from
            (select
                ITEM_ID,
                name,
                price,
                null as actor,
                null as director,
                author,
                isbn,
                2 as clazz_ 
            from
                Book) i1_0 
        where
            i1_0.clazz_=2
item.getName() = JPA
item.getPrice() = 1000
```
- 위의 두 전략과는 다르게 `DTYPE`을 통해 만족하는 조건을 찾는 것이 아니라 `'clazz_'` 라는 속성을 통해 만족하는 조건을 찾는 것을 확인할 수 있었다.
- 이 clazz_는 TABLE_PER_CLASS 전략을 사용할 때만 생성
- `Book, Movie` 각 테이블의 clazz_ 1, 2 로 설정 확인 

### JPQL 기타

- `SQL`과 문법이 같은 식
- EXISTS, IN
- AND, OR, NOT
- `=, >, >=, <, <=, <>`이 clazz_는 TABLE_PER_CLASS 전략을 사용할 때만 생성
- BETWEEN, LIKE, IS NULL