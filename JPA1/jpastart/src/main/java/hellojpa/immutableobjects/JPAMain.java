package hellojpa.immutableobjects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JPAMain {
    public static void main(String[] args) {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
       EntityManager em = emf.createEntityManager();
       EntityTransaction tx = em.getTransaction();
       tx.begin();

       try {
           Address address = new Address("city", "street", "10000");

           Test member1 = new Test();
           member1.setName("member1");
           member1.setAddress(address);
           em.persist(member1);

           Test member2 = new Test();
           member2.setName("member2");
           member2.setAddress(address);
           em.persist(member2);

           // Address 불변 객체로 생성 setter 가 없다.
           Address updateAddress = new Address("newCity", address.getStreet(), address.getZipcode());
           member2.setAddress(updateAddress);

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
