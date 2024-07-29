package hellojpa.immutableobjects;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;
    public Address() {
    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
    public String getCity() {
        return city;
    }
    public String getStreet() {
        return street;
    }
    public String getZipcode() {
        return zipcode;
    }

    /**
     * 인텔리제이 자동 generate 오버라이드 equals()메서드 분석
     * this == o return true : 자신의 값을 비교하니 당연히 true
     * 만약 비교 객체가 null 이거나 getClass(): 클래스 메타 데이터가 다르면 false 즉 해당 객체의 설계도(클래스)가 다르면
     * Object.equals() 내부 동작
     * 두 객체가 모두 null인 경우: true를 반환
     * 한 객체만 null인 경우: false를 반환
     * 두 객체가 모두 null이 아닌 경우: 두 객체의 equals() 메서드를 호출하여 비교
     * 여기서는 String equals() 메서드 호출하여 비교
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
    }

    /**
     * Objects.hash()는 느리다 -> 만약 성능상 문제가 생기면 직접 구현
     * 이제 물리적(참조값)이 서로 다르더라도 논리적(인스턴스의 값들)같으면 컬렉션은 같은거라고 인지한다.
     * 또한 Set 같이 해시코드를 기반으로 자료를 저장하는 컬렉션 또한
     * 같은 값을 넣은 2개의 객체를 넣으면 1개만 저장된다.
     */
    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipcode);
    }
}
