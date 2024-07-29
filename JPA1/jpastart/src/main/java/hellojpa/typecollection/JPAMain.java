package hellojpa.typecollection;

import hellojpa.immutableobjects.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

public class JPAMain {
    public static void main(String[] args) {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
       EntityManager em = emf.createEntityManager();
       EntityTransaction tx = em.getTransaction();
       tx.begin();

       try {
           People people = new People();
           people.setUsername("people1");
           people.setHomeAddress(new Address("homeCity", "street", "1"));

           people.getFavoriteFoods().add("치킨");
           people.getFavoriteFoods().add("족발");
           people.getFavoriteFoods().add("피자");

           people.getAddressHistory().add(new Address("old1", "street", "2"));
           people.getAddressHistory().add(new Address("old1", "street", "3"));

           em.persist(people);

           em.flush();
           em.clear();

           System.out.println("======히또======");
           People findPeople = em.find(People.class, people.getId());

           List<Address> addressHistory = findPeople.getAddressHistory();
           for (Address address : addressHistory) {
               System.out.println("address.city = " + address.getCity());
           }

           Set<String> favoriteFoods = findPeople.getFavoriteFoods();
           System.out.println(favoriteFoods.getClass());
           for (String favoriteFood : favoriteFoods) {
               System.out.println("favoriteFood = " + favoriteFood);
           }

           System.out.println("====START=====");
           findPeople.getAddressHistory().remove(new Address("old1", "street", "2"));
           findPeople.getAddressHistory().add(new Address("newCity1", "street", "2"));

           tx.commit();
       }
       catch (Exception e){
           tx.rollback();
       }
       finally {
           em.close();
       }
       emf.close();
   }
}
