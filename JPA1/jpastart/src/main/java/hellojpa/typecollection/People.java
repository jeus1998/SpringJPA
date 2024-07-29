package hellojpa.typecollection;

import hellojpa.immutableobjects.Address;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class People {
    @Id
    @GeneratedValue
    @Column(name = "PEOPLE_ID")
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @Embedded
    @Column(name = "HOME_ADDRESS")
    private Address homeAddress;
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD" , joinColumns =
            @JoinColumn(name = "PEOPLE_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();
    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns =
            @JoinColumn(name = "PEOPLE_ID")
    )
    private List<Address> addressHistory = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Address getHomeAddress() {
        return homeAddress;
    }
    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }
    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }
    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }
    public List<Address> getAddressHistory() {
        return addressHistory;
    }
    public void setAddressHistory(List<Address> addressHistory) {
        this.addressHistory = addressHistory;
    }
}
