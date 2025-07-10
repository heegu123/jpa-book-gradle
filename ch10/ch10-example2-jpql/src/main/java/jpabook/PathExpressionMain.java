package jpabook;

import jakarta.persistence.*;
import jpabook.model.entity.Member;
import jpabook.model.entity.Order;
import jpabook.model.entity.Product;
import jpabook.model.entity.Team;
import jpabook.model.vo.Address;

import java.util.List;

public class PathExpressionMain {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
    private static final EntityManager em = emf.createEntityManager();
    private static final EntityTransaction tx = em.getTransaction();

    public static void main(String[] args) {
        try {
            tx.begin();
            init();
            tx.commit();

            // 상태 필드 경로탐색
            stateFiledPathExpression();

            // 단일 값 연관 경로 탐색
            singleValuedAssociationPathExpression();

            // 복잡한 단일 값 연관 경로 탐색
            singleValuedAssociationPathExpression2();

            // 컬렉션 값 연관경로 탐색 - jpa collection-valued path expression
            collectionValueAssociationPathExpression();

            collectionValueAssociationPathExpression2();

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

    // 상태 필드 경로탐색 - jpa state field path expression
    public static void stateFiledPathExpression() {
        String jpql = "select m.name, m.age from Member m";
        List<Object[]> resultList = em.createQuery(jpql).getResultList();

        System.out.println("\n***** 상태 필드 경로 탐색 *****");
        for (Object[] objects : resultList) {
            String name = (String) objects[0];
            int age = (Integer) objects[1];
            System.out.println("name = " + name + ", age = " + age);
        }
    }

    // 단일 값 연관 경로 탐색 - jpa single-valued association path
    public static void singleValuedAssociationPathExpression() {
        String jpql = "select o.member from Order o";

        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        System.out.println("\n***** 단일 값 연관 경로 탐색 *****");
        for (Member member : resultList) {
            System.out.println("order member = " + member);
        }
    }

    // 예제 10.37 복잡한 단일 값 연관 경로 탐색2 - jpa single-valued association path
    public static void singleValuedAssociationPathExpression2() {
        String jpql = "select o.member.team from Order o " +
                "where o.product.name = 'product1' and o.address.city = 'city1'";

        Team team = em.createQuery(jpql, Team.class).getSingleResult();

        System.out.println("\n***** 복잡한 단일 값 연관 경로 탐색 *****");
        System.out.println("team = " + team);
    }

    // 컬렉션 값 연관경로 탐색 - jpa collection-valued path expression
    public static void collectionValueAssociationPathExpression() {
        String jpql = "select t.members from Team t";

        System.out.println("\n***** 컬렉션 값 연관경로 탐색 *****");
        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }

    // 컬렉션 값 연관경로 탐색, size 사용(hibernate 6.x 버전부터는 size()) - jpa collection-valued path expression
    public static void collectionValueAssociationPathExpression2() {
        String jpql = "select t.name, size(t.members) from Team t";

        System.out.println("\n***** 컬렉션 값 연관경로 탐색 - size 사용*****");
        List<Object[]> resultList = em.createQuery(jpql).getResultList();

        for (Object[] objects : resultList) {
            String teamName = (String) objects[0];
            Integer memberCount = (Integer) objects[1];

            System.out.println("teamName = " + teamName + ", memberCount = " + memberCount);
        }
    }
}