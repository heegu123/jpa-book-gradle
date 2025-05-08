package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Member;
import jpabook.model.entity.PhoneServiceProvider;
import jpabook.model.vo.Address;
import jpabook.model.vo.PhoneNumber;
import jpabook.model.vo.Zipcode;

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

            member.setName("Kim");

            Zipcode zipcode = new Zipcode("12345", "1234");
            Address address = new Address("Seoul", "sejong-ro", zipcode);

            PhoneServiceProvider provider = new PhoneServiceProvider();
            provider.setName("provider1");

            PhoneNumber phoneNumber = new PhoneNumber("+82", "010-1234-5678", provider);

            member.setAddress(address);
            member.setPhoneNumber(phoneNumber);

            em.persist(member);
            em.persist(provider);

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