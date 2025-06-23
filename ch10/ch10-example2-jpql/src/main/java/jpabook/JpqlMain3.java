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

            // 예제 10.24 - Inner Join
            innerJoin();

            // 예제 10.25 - Outer Join
            outerJoin();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void init() {

        Team team1 = new Team();
        team1.setName("팀A");
        em.persist(team1);

        Team team2 = new Team();
        team2.setName("팀B");
        em.persist(team2);

        Member member1 = new Member();
        member1.setAge(10);
        member1.setName("회원1");
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member();
        member2.setAge(20);
        member2.setName("회원2");
        member2.setTeam(team1);
        em.persist(member2);

        Member member3 = new Member();
        member3.setAge(30);
        member3.setName("회원3");
        member3.setTeam(team2);
        em.persist(member3);

        Member member4 = new Member();
        member3.setAge(40);
        member3.setName("회원4");
        em.persist(member4);

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

    // 예제 10.24 - Inner Join
    public static void innerJoin() {

        String teamName = "팀A";

        String query = "SELECT m " +
                "FROM Member m INNER JOIN m.team t " +
                "WHERE t.name = :teamName";

        String query2 = "SELECT m.name, t.name " +
                "FROM Member m INNER JOIN m.team t " +
                "WHERE t.name = :teamName  " +
                "ORDER BY m.age DESC ";

        List<Member> resultList = em.createQuery(query, Member.class)
                .setParameter("teamName", teamName)
                .getResultList();

        List<Object[]> resultList2 = em.createQuery(query2)
                .setParameter("teamName", teamName)
                .getResultList();

        System.out.println("\n***** Ex10.24 내부 조인 사용 예 *****\n");
        for (Member member : resultList) {
            System.out.println("member = " + member);
        }

        for (Object[] objects : resultList2) {
            String mName = (String) objects[0];
            String tName = (String) objects[1];
            System.out.println("mName, tName = " + mName + ", "+ tName);
        }
    }

    // 예제 10.25 - Outer Join
    public static void outerJoin() {
        String query = "SELECT m " +
                "FROM Member m LEFT JOIN m.team t";

        List<Object[]> resultList = em.createQuery(query).getResultList();


        System.out.println("\n***** Ex10.25 외부 조인 사용 예 *****\n");
        for (Object[] objects : resultList) {
            Member m = (Member) objects[0];
            System.out.println("member = " + m);
        }
    }
}