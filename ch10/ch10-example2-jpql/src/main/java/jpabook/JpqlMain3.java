package jpabook;

import jakarta.persistence.*;
import jpabook.model.entity.*;

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

            // 예제 - Collection Join
            collectionJoin();

            // 예제 10.26 - Theta Join(Cross Join, Cartesian Product)
            thetaJoin();

            // 예제 10.27 - JOIN ON 절
            joinOn();

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
        String query = "SELECT m, t " +
                "FROM Member m LEFT JOIN m.team t";

        List<Object[]> resultList = em.createQuery(query).getResultList();


        System.out.println("\n***** Ex10.25 외부 조인 사용 예 *****\n");
        for (Object[] objects : resultList) {
            Member m = (Member) objects[0];
            Team t = (Team) objects[1];
            System.out.println("member, team = " + m + ", " + t);
        }
    }

    // 예제 Collection Join
    public static void collectionJoin() {
        String query = "SELECT t, m FROM Team t LEFT JOIN t.members m";
        List<Object[]> resultList = em.createQuery(query).getResultList();

        System.out.println("\n***** Ex10.26 컬렉션 조인 사용 예 *****\n");
        for (Object[] objects : resultList) {
            Team t = (Team) objects[0];
            Member m = (Member) objects[1];

            System.out.println("Team, Member = " + t + ", " + m);
        }
    }

    // 예제 10.26 - Theta Join(Cross Join, Cartesian Product)
    public static void thetaJoin() {
        String query = "SELECT COUNT(m) " +
                "FROM Member m, Team t ";

        System.out.println("\n***** Ex10.27 세타 조인 사용 예 *****\n");
        Long singleResult = em.createQuery(query, Long.class).getSingleResult();

        System.out.println("singleResult = " + singleResult);
    }

    // 예제 10.27 - JOIN ON 절
    public static void joinOn() {

        String teamName = "팀A";
        Query query = em.createQuery("SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = :teamName");
        query.setParameter("teamName", teamName);
        List<Object[]> resultList = query.getResultList();

        System.out.println("\n***** Ex10.27 JOIN ON절 사용 예 *****\n");
        for (Object[] objects : resultList) {
            Member member = (Member) objects[0];
            Team team = (Team) objects[1];

            System.out.println("Member, Team = " + member + ", " + team);
        }
    }
}