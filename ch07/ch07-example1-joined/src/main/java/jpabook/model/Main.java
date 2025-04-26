package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Album;
import jpabook.model.entity.Book;
import jpabook.model.entity.Movie;

/**
 * Created by 1001218 on 15. 4. 5..
 */
public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Album album = new Album();
            album.setName("Album1");
            album.setPrice(10000);
            album.setArtist("Artist1");
            em.persist(album);

            Book book = new Book();
            book.setName("Book1");
            book.setPrice(15000);
            book.setAuthor("Author1");
            book.setIsbn("123456");
            em.persist(book);

            Movie movie = new Movie();
            movie.setName("Movie1");
            movie.setPrice(20000);
            movie.setDirector("Director1");
            movie.setActor("Actor1");
            em.persist(movie);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}