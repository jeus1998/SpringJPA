package hellojpa;

import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {
    /**
     * PK: @Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    /**
     * @Column(name ="name") 객체는 username / 데이터베이스 컬럼명은 name
     */
    @Column(name = "name", length = 10)
    private String username;

    private Integer age;
    /**
     * DB에는 Enum 타입이 없다. 데이터베이스에 저장할 때 문자열 형태로 저장하도록 지정
     */
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    /**
     * @Temporal : 자바 Date 에는 날짜, 시간 함께 있다.
     * DB에는 3가지 타입 존재 (DATE: 날짜 / TIME: 시간 / TIMESTAMP: 날짜 + 시간)
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifedDate;
    /**
     * @Lob 해당 필드가 데이터베이스의(LOB: Large Object)타입과 매핑된다는 의미
     */
    @Lob
    private String description;

    private LocalDateTime test1;
    private LocalDate test2;
    private Time test3;
    public Member(){

    }
}
