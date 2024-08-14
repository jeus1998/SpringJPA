# Querydsl 설정과 검증

### Gradle 전체 설정 - 스프링 부트 3.x

```gradle
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'

	// sql 쿼리 바인딩 라이브러리
	implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'

	// 테스트 롬복 사용
	testCompileOnly 'org.projectlombok:lombok'
	testAnnotationProcessor 'org.projectlombok:lombok'

	// Querydsl 추가
	implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
	annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
	annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	annotationProcessor "jakarta.persistence:jakarta.persistence-api"
}
```
- [Querydsl 스프링 부트 3.0 관련 설정 방법](https://bit.ly/springboot3)

## Querydsl 환경설정 검증

### 검증용 엔티티 생성

```java
@Entity
@Getter @Setter
public class Hello {
    @Id @GeneratedValue
    private Long id;
}
```

### 검증용 Q 타입 생성

Gradle IntelliJ 사용법
- Gradle ➡️ Tasks ➡️ build ➡️ clean
- Gradle ➡️ Tasks ➡️ other ➡️ compileQueryds

Gradle 콘솔 사용법
- ./gradlew clean compileQuerydsl


최신버전
- compileQueryds이 사라졌다. 
- 빌드 이후 바로 Q 타입 생성 확인 


Q 타입 생성 확인
- build ➡️ generated ➡️ querydsl
- `study.querydsl.entity.QHello.java` 파일이 생성되어 있어야 함

참고
- Q타입은 컴파일 시점에 자동 생성되므로 버전관리(GIT)에 포함하지 않는 것이 좋다.
- 앞서 설정에서 생성 위치를 gradle build 폴더 아래 생성되도록 했기 때문에 이 부분도 자연스럽게 해결된다.
- 대부분 gradle build 폴더를 git에 포함하지 않는다.

### 테스트 케이스로 실행 검증

```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class QuerydslApplicationTests {
	@Autowired
	EntityManager em;
	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QHello qHello = new QHello("h");

		Hello result = query
				.selectFrom(qHello)
				.fetchOne();

		assertThat(result).isEqualTo(hello);
	}
}
```
- 참고: 스프링 부트에 아무런 설정도 하지 않으면 h2 DB를 메모리 모드로 JVM안에서 실행한다.
