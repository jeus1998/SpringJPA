# Auditing

### Auditing?

- 엔티티를 생성, 변경할 때 변경한 사람과 시간을 추적하고 싶으면?
  - 등록일
  - 수정일
  - 등록자
  - 수정자

## 순수 JPA 사용 

### JpaBaseEntity

```java
@MappedSuperclass // 진짜 상속관계가 아닌 속성만 공유
@Getter
public class JpaBaseEntity {
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }
    @PreUpdate
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }
}
```

### Member extends JpaBaseEntity

```java
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member extends JpaBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;
    // 생략 ...
}    
```

### 순수 JPA 테스트 

```java
@Test
public void JpaEventBaseEntity() throws InterruptedException{
    // given
    Member member = new Member("member1");
    memberJpaRepository.save(member); // @PrePersist

    Thread.sleep(1000);
    member.setUsername("member2");

    em.flush(); // @PreUpdate
    em.clear();

    // when
    Member findMember = memberJpaRepository.findById(member.getId()).get();

    // then
    System.out.println("findMember.createdDate = " + findMember.getCreatedDate());
    System.out.println("findMember.updatedDate = " + findMember.getUpdatedDate());
}
```

### JPA 주요 이벤트 애노테이션 

- @PrePersist
  - 엔티티가 처음으로 영속성 컨텍스트에 저장되기 전에 실행
- @PostPersist
  - 엔티티가 영속성 컨텍스트에 저장된 후에 실행
- @PreUpdate
  - 엔티티의 상태가 변경되어 업데이트되기 전에 실행
- @PostUpdate
  - 엔티티가 업데이트된 후에 실행
- @PreRemove
  - 엔티티가 삭제되기 전에 실행
- @PostRemove
  - 엔티티가 삭제된 후에 실행
- @PostLoad
  - 엔티티가 영속성 컨텍스트로 로드된 후에 실행

## 스프링 데이터 JPA 사용

### 설정

- `@EnableJpaAuditing` ➡️ 스프링 부트 설정 클래스에 적용해야함
- `@EntityListeners(AuditingEntityListener.class)` ➡️ 엔티티에 적용

### 스프링 부트 설정 클래스에 적용

```java
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

}
```

### BaseEntity - 스프링 데이터 Auditing 적용

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
```

### 등록자, 수정자를 처리해주는 AuditorAware 스프링 빈 등록

```java
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}
	/**
	 * 실무에서는 세션 정보, 스프링 시큐리티 로그인 정보에서 ID를 받음
	 */
	@Bean
	public AuditorAware<String> auditorProvider(){
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
```

### 실무 

- 실무에서 대부분의 엔티티는 등록시간, 수정시간이 필요
- 하지만 등록자, 수정자는 없을 수도 있다.
- 그래서 Base 타입을 분리하고, 원하는 타입을 선택해서 상속한다.

```java

public class BaseTimeEntity {
   @CreatedDate
   @Column(updatable = false)
   private LocalDateTime createdDate;
   @LastModifiedDate
   private LocalDateTime lastModifiedDate;
}

public class BaseEntity extends BaseTimeEntity {
     @CreatedBy
     @Column(updatable = false)
     private String createdBy;
     @LastModifiedBy
     private String lastModifiedBy;
}
```
