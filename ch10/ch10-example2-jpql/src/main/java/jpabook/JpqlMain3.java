package jpabook;

import jakarta.persistence.*;
import jpabook.model.entity.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JpqlMain3 {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    private static final EntityManager em = emf.createEntityManager();
    private static final EntityTransaction tx = em.getTransaction();

    public static void main(String[] args) {
        try {
            tx.begin();
            init();
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void init() {

        Team team = new Team();
        team.setName("팀A");
        em.persist(team);
        Team team2 = new Team();
        team2.setName("팀B");
        em.persist(team2);

        Member member1 = new Member();
        member1.setAge(10);
        member1.setName("회원1");
        member1.setTeam(team);
        em.persist(member1);

        Member member2 = new Member();
        member2.setAge(20);
        member2.setName("회원2");
        member2.setTeam(team);
        em.persist(member2);

        Member member3 = new Member();
        member3.setAge(30);
        member3.setName("회원3");
        member3.setTeam(team2);
        em.persist(member3);

        Product product1 = new Product();
        product1.setName("Product1");
        product1.setPrice(10000);
        product1.setStockAmount(10);
        em.persist(product1);

        Order order1 = new Order();
        order1.setOrderAmount(10);
        order1.setMember(member1);
        order1.setProduct(product1);
        em.persist(order1);
    }


}