package hellojpa.immutableobjects;

import jakarta.persistence.*;

/**
 * 임베디드 타입 테스트 용
 */
@Entity
public class Test {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Embedded
    private Period period;
    @Embedded
    private Address address;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
