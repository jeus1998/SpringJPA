package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/**
 * 불변 객체 Address
 * equals & hashcode 사용
 */
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;

    /**
     * 값 타입을 의미있게 사용하는 비즈니스 메서드
     */
    public String fullAddress(){
        return getCity() + getStreet() + getZipcode();
    }
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
     * JPA는 프록시를 사용하기 때문에
     * 객체.city 이렇게 접근하는게 아닌 getter()를 사용해서 직접 참조하는 객체(target)를 호출하도록 하자
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }
    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipcode);
    }
}
