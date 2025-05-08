package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Member;
import jpabook.model.vo.Address;

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

            Member member = new Member();

            Address address = new Address("Seoul", "Solsaem-ro", "12345-6789");
            member.setName("Kim");
            member.setHomeAddress(address);

            member.getFavoriteFoods().add("Chicken");
            member.getFavoriteFoods().add("Pizza");

            member.getAddressHistory().add(address);
            member.getAddressHistory().add(new Address("Incheon", "Songdo-ro", "11111-2222"));

            em.persist(member);

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