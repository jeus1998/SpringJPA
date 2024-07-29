package hellojpa.type;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 임베디드 타입 테스트 용
 */
// @Entity
public class Test {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Embedded
    private Period period;
    @Embedded
    private Address address;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIP_CODE"))
    })
    private Address workAddress;
}
