# 변경 감지와 병합(merge)

### 준영속 엔티티?

- 영속성 컨텍스트가 관리하지 않는 엔티티를 말한다

```java
@PostMapping("/items/{itemId}/edit")
public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

    Book book = new Book();
    book.setId(form.getId());
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);
    return "redirect:/items";
}
```
- `itemService.saveItem(book)`에서 수정을 시도하는 `Book` 객체다.
- `Book` 객체는 이미 `DB`에 한번 저장되어서 식별자가 존재한다. 
- 이렇게 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.

### 준영속 엔티티를 수정하는 2가지 방법

- 변경 감지 기능 사용
- 병합(merge) 사용

### 병합 사용

- 병합은 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능이다.

```java

// Controller 코드 
itemService.saveItem(book);

// Service 
@Transactional
public void saveItem(Item item){
    itemRepository.save(item);
}

// Repository
private final EntityManager em;

public void save(Item item){ 
    if(item.getId() == null){
        em.persist(item);
    }
    else{
        em.merge(item);
    }
}
```

### 병합 동작 방식

![2.png](Image%2F2.png)

- merge()를 실행한다.
- 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회한다.
- 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고, 1차 캐시에 저장한다.
- 조회한 영속 엔티티(mergeMember)에 member 엔티티의 값을 채워 넣는다. 
  - `member` 엔티티의 모든 값을 `mergeMember`에 밀어 넣는다. 
- 영속 상태인 `mergeMember`를 반환한다.

### 병합시 동작 방식을 간단히 정리

1. 준영속 엔티티의 식별자 값으로 영속 엔티티를 조회한다.
2. 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체한다.(병합한다.)
3. 트랜잭션 커밋 시점에 변경 감지 기능이 동작해서 데이터베이스에 `UPDATE SQL`이 실행

주의!
- 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다.
- 병합시 값이 없으면 `null` 로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체한다.)

### 상품 리포지토리의 저장 메서드 분석 `ItemRepository`

```java
@Repository
public class ItemRepository {
    @PersistenceContext
    EntityManager em;
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } 
        else {
            em.merge(item);
        }
    }
    
    //...
}
```
- save() 메서드는 식별자 값이 없으면(null) 새로운 엔티티로 판단해서 영속화(persist)하고 식별자가 있으면 병합(merge)
- 지금처럼 준영속 상태인 상품 엔티티를 수정할 때는 id 값이 있으므로 병합 수행

새로운 엔티티 저장과 준영속 엔티티 병합을 편리하게 한번에 처리
- 상품 리포지토리에선 save() 메서드를 유심히 봐야 하는데, 이 메서드 하나로 저장과 수정(병합)을 다 처리한다.
- 코드를 보면 식별자 값이 없으면 새로운 엔티티로 판단해서 persist()로 영속화하고 만약 식별자 값이 있으면 이미 한번
  영속화 되었던 엔티티로 판단해서 merge()로 수정(병합)한다.
- 결국 여기서의 저장(save)이라는 의미는 신규 데이터를 저장하는 것뿐만 아니라 변경된 데이터의 저장이라는 의미도 포함한다.
- 이렇게 함으로써 이 메서드를 사용하는 클라이언트는 저장과 수정을 구분하지 않아도 되므로 클라이언트의 로직이 단순해진다.
- 여기서 사용하는 수정(병합)은 준영속 상태의 엔티티를 수정할 때 사용한다.
- 영속 상태의 엔티티는 변경 감지(dirty checking)기능이 동작해서 트랜잭션을 커밋할 때 자동으로 수정되므로 별도의 수정 메서드를 호출할 필요가 없고 그런
  메서드도 없다.

참고 
- save() 메서드는 식별자를 자동 생성해야 정상 동작한다. 
- 여기서 사용한 `Item` 엔티티의 식별자는 자동으로 생성되도록 `@GeneratedValue`를 선언했다.
- 따라서 식별자 없이 save() 메서드를 호출하면 persist()가 호출되면서 식별자 값이 자동으로 할당된다.
- 반면에 식별자를 직접 할당하도록 `@Id` 만 선언했다고 가정하자
- 이 경우 식별자를 직접 할당하지 않고, save() 메서드를 호출하면 식별자가 없는 상태로 persist()를 호출한다. 
  그러면 식별자가 없다는 예외가 발생한다.

참고
- 실무에서는 보통 업데이트 기능이 매우 제한적이다. 
- 그런데 병합은 모든 필드를 변경해버리고, 데이터가 없으면 `null`로 업데이트 해버린다. 
- 병합을 사용하면서 이 문제를 해결하려면, 변경 폼 화면에서 모든 데이터를 항상 유지해야 한다.
- 실무에서는 보통 변경가능한 데이터만 노출하기 때문에, 병합을 사용하는 것이 오히려 번거롭다.

### 변경 감지 기능 사용

```java
/**
 * Dirty Checking 변경 감지 사용
 */
@PostMapping("/items/{itemId}/edit")
public String updateItemV2(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
  itemService.update(form);
  return "redirect:/items";
}

// 서비스 코드 
@Transactional
public void update(BookForm form) {
    Book book = (Book) itemRepository.findOne(form.getId());
    book.setName(form.getName());
    book.setIsbn(form.getIsbn());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
}
```
- 영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법
- 트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 ➡️ 트랜잭션 커밋 시점에 변경 감지(Dirty Checking)이 동작해서
  데이터베이스에 UPDATE SQL 실행

### 가장 좋은 해결 방법

- 엔티티를 변경할 때는 항상 변경 감지를 사용하자
- 컨트롤러에서 어설프게 엔티티를 생성 ❌
- 트랜잭션이 있는 서비스 계층에 식별자(id)와 변경할 데이터를 명확하게 전달(파라미터 or dto)
- 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경
- 트랜잭션 커밋 시점에 변경 감지가 실행

### 수정 로직 또한 엔티티 내부에 만들어 두자 

1. setter 최소화 
2. 메서드 추적 용이 
3. 의미있는 메서드를 통해서 개발자 또한 식별 편이 
4. 변경 감지 사용 - `merge` 사용으로 데이터 손실 사전 차단
5. 정확한 파라미터를 요구하여 어떤 데이터가 수정될것인지 인지 할 수 있다. 



