package jpql;

import jakarta.persistence.*;
import jpql.domain.*;
import jpql.dto.MemberDto;
import jpql.test.Book;
import jpql.test.Item;
import jpql.test.Movie;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static jpql.domain.MemberType.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Book book = new Book();
            book.setName("JPA");
            book.setPrice(1000);
            em.persist(book);

            Movie movie = new Movie();
            movie.setName("300");
            movie.setPrice(100000);
            em.persist(movie);

            em.flush();
            em.clear();

            List<Item> resultList =
                    em.createQuery("select i from Item i where type(i)=Book")
                    .getResultList();

            for (Item item : resultList) {
                System.out.println("item.getName() = " + item.getName());
                System.out.println("item.getPrice() = " + item.getPrice());
            }

            tx.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        finally {
            em.close();
        }
        emf.close();
    }
}
