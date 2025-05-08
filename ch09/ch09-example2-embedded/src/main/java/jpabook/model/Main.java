package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Address;
import jpabook.model.entity.Member;
import jpabook.model.entity.Period;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
            Period workperiod = new Period(LocalDateTime.now(), LocalDateTime.now().plusDays(10));
            member.setWorkPeriod(workperiod);
            member.setHomeAddress(new Address("서울","청와대로", "1"));

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