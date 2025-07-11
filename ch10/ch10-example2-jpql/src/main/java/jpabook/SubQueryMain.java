package jpabook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Member;
import jpabook.model.entity.Order;
import jpabook.model.entity.Product;
import jpabook.model.entity.Team;
import jpabook.model.vo.Address;

import java.util.List;

public class SubQueryMain {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    private static final EntityManager em = emf.createEntityManager();
    private static final EntityTransaction tx = em.getTransaction();

    public static void main(String[] args) {
        try {
            tx.begin();
            init();
            tx.commit();

            // where 절 서브쿼리 1
            subquery1();

            subquery2();

            subquery3();

            subquery4();

            // 서브쿼리 함수
            subqueryFunc1();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // 데이터 준비
    public static void init() {

        Team team1 = new Team();
        team1.setName("팀A");
        em.persist(team1);

        Team team2 = new Team();
        team2.setName("팀B");
        em.persist(team2);

        Team team3 = new Team();
        team3.setName("팀C");
        em.persist(team3);

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
        member4.setAge(40);
        member4.setName("회원4");
        em.persist(member4);

        Product product1 = new Product();
        product1.setName("Product1");
        product1.setPrice(10000);
        product1.setStockAmount(10);
        em.persist(product1);

        Address address = new Address("city1", "street1", "zip-code");

        Order order1 = new Order();
        order1.setOrderAmount(10);
        order1.setAddress(address);
        order1.setMember(member1);
        order1.setProduct(product1);
        em.persist(order1);
    }
    // where 절 서브쿼리 1
   public static void subquery1() {
       String jpql = "select m from Member m" +
               " where m.age >" +
               " (select avg(m2.age) from Member m2)";

       String jpql2 = "select avg(m.age) from Member m";

       System.out.println("\n***** where 절 서브쿼리 1 *****");
       List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();
       Double singleResult = em.createQuery(jpql2, Double.class).getSingleResult();

       System.out.println("AVG Age = " + singleResult);

       for (Member member : resultList) {
           System.out.println("member = " + member);
       }
   }
   // where 절 서브쿼리 2
    public static void subquery2() {
        String jpql = "select avg(m.age) from Member m";

        System.out.println("\n***** where 절 서브쿼리 2 *****");
        List<Object[]> resultList = em.createQuery(jpql).getResultList();
    }
    // where 절 서브쿼리3
    public static void subquery3() {
        String jpql = "select m " +
                " from Member m" +
                " where (select count(o) from Order o where m = o.member) > 0";

        System.out.println("\n***** where 절 서브쿼리 3 *****");
        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }
    // where 절 서브쿼리4
    public static void subquery4() {
        String jpql = "select m from Member m where size(m.orders) > 0";

        System.out.println("\n***** where 절 서브쿼리 4 *****");
        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }

    // 서브쿼리함수 - [NOT]EXISTS(subquery)
    public static void subqueryFunc1() {
        String jpql = "select m from Member m" +
                " where exists (select t from m.team t where t.name='팀A')";

        System.out.println("\n***** 서브쿼리함수 - EXISTS *****");
        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }
    // 서브쿼리함수 - ALL/ANY
}