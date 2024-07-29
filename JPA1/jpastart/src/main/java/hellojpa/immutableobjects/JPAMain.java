package hellojpa.immutableobjects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.HashSet;
import java.util.Set;

public class JPAMain {
    public static void main(String[] args) {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
       EntityManager em = emf.createEntityManager();
       EntityTransaction tx = em.getTransaction();
       tx.begin();

       try {
           Address address1 = new Address("city", "street", "10000");
           Address address2 = new Address("city", "street", "10000");

           System.out.println(address1.equals(address2)); // true

           Set<Address> set = new HashSet<>();
           set.add(address1);
           set.add(address2);

           System.out.println(set.size()); // 1

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
