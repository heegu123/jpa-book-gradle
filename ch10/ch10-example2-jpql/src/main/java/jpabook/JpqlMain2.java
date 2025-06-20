package jpabook;

import jakarta.persistence.*;
import jpabook.model.entity.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JpqlMain2 {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    private static final EntityManager em = emf.createEntityManager();
    private static final EntityTransaction tx = em.getTransaction();

    public static void main(String[] args) {
        try {
            tx.begin();
            init();
            tx.commit();

            //에제 10.8
            typedQueryEx();

            //에제 10.9
            queryEx();

            //에제 10.10
            namedParamEx();

            //에제 10.11
            positionalParam();

            //에제 10.12
            multiValueProjectionEx1();

            //에제 10.13
            multiValueProjectionEx2();

            //에제 10.14
            multiValueProjectionEx3();

            //에제 10.15
            multiValueProjectionEx4();

            //에제 10.17
            multiValueProjectionEx5();

            //에제 10.18
            paging();

            // groupby
            grouping();

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

    // 예제 10.8
    public static void typedQueryEx() {

        TypedQuery<Member> query = em.createQuery("SELECT m FROM Member AS m", Member.class);

        List<Member> resultList = query.getResultList();

        System.out.println("\n***** Ex10.8 TypedQueryEx *****\n");
        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }

    // 예제 10.9
    public static void queryEx() {

        Query query = em.createQuery("SELECT m.name, m.age FROM Member AS m");

        List resultList = query.getResultList();

        System.out.println("\n***** Ex10.9 QueryEx *****\n");
        for (Object o : resultList) {
            Object[] result = (Object[]) o;
            System.out.println("name = " + result[0]);
            System.out.println("age = " + result[1]);
        }
    }
    
    //예제 10.10
    public static void namedParamEx() {

        String nameParam = "Kim";

        TypedQuery<Member> query =
                em.createQuery("select m from Member m where m.name = :name", Member.class);

        List<Member> resultList = query.setParameter("name", nameParam)
                                       .getResultList();

        System.out.println("\n***** Ex10.10 NamedParameterEx *****\n");
        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }
    
    //예제 10.11
    public static void positionalParam() {
        String nameParam = "Kim";

        List<Member> resultList = em.createQuery("SELECT m FROM Member m WHERE m.name = ?1", Member.class)
                .setParameter(1, nameParam)
                .getResultList();

        System.out.println("\n***** Ex10.11 PositionParameterEx *****\n");
        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }

    //예제 10.12 - 여러 값 조회
    public static void multiValueProjectionEx1() {
        Query query = em.createQuery("SELECT m.name, m.age FROM Member m");
        List resultList = query.getResultList();

        Iterator iterator = resultList.iterator();

        System.out.println("\n***** Ex10.12 MultiValueProjectionEx1 *****\n");
        while (iterator.hasNext()) {
            Object[] row = (Object[]) iterator.next();
            String name = (String) row[0];
            Integer age = (Integer) row[1];
            System.out.println("name, age = " + name + ", " + age);
        }
    }

    //예제 10.13 Object[]로 조회
    public static void multiValueProjectionEx2() {
        List<Object[]> resultList = em.createQuery("SELECT m.name, m.age FROM Member m")
                .getResultList();

        System.out.println("\n***** Ex10.13 MultiValueProjectionEx2 *****\n");
        for (Object[] row : resultList) {
            String name = (String) row[0];
            Integer age = (Integer) row[1];
            System.out.println("name, age = " + name + ", " + age);
        }
    }

    //예제 10.14 - 여러 프로젝션 엔티티 타입 조회
    public static void multiValueProjectionEx3() {
        List<Object[]> resultList =
                em.createQuery("SELECT o.member, o.product, o.orderAmount from Order o")
                        .getResultList();

        System.out.println("\n***** Ex10.14 MultiValueProjectionEx3 *****\n");
        for (Object[] row : resultList) {
            Member member = (Member) row[0];
            Product product = (Product) row[1];
            int orderAmount = (Integer) row[2];
            System.out.println("member, product, orderAmount = " + member + ", " + product + ", " + orderAmount);
        }
    }

    //예제 10.15
    public static void multiValueProjectionEx4() {

        List<Object[]> resultList =
                em.createQuery("SELECT m.name, m.age FROM Member AS m").getResultList();

        List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
        for (Object[] objects : resultList) {
            MemberDTO memberDTO = new MemberDTO((String) objects[0], (Integer) objects[1]);
            memberDTOs.add(memberDTO);
        }

        System.out.println("\n***** Ex10.15 MultiValueProjectionEx4 *****\n");
        for (MemberDTO dto : memberDTOs) {
            System.out.println(dto);
        }
    }

    //예제 10.17
    public static void multiValueProjectionEx5() {

        TypedQuery<MemberDTO> query = em.createQuery("select new jpabook.model.entity.MemberDTO(m.name, m.age) from Member m", MemberDTO.class);
        List<MemberDTO> resultList = query.getResultList();

        System.out.println("\n***** Ex10.17 MultiValueProjectionEx4 *****\n");
        for (MemberDTO dto : resultList) {
            System.out.println(dto);
        }
    }

    // 예제 10.18 - 페이징 API
    public static void paging() {
        TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m ORDER BY m.id ASC", Member.class);

        query.setFirstResult(1);
        query.setMaxResults(1);
        List<Member> resultList = query.getResultList();

        System.out.println("\n***** Ex10.18 paging *****\n");
        for (Member member : resultList) {
            System.out.println(member);
        }
    }

    public static void grouping() {
        Query query = em.createQuery("SELECT m.name, count(m), sum(m.age) FROM Member m GROUP BY m.name");
        List<Object[]> resultList = query.getResultList();

        System.out.println("\n***** grouping *****\n");
        for (Object[] row : resultList) {
            String name = (String) row[0];
            Long count = (Long) row[1];
            Long age = (Long) row[2];
            System.out.println("name = " + name + ", count = " + count + ", age = " + age);
        }
    }
}