package jpabook;

import jakarta.persistence.*;
import jpabook.model.entity.Member;
import jpabook.model.entity.MemberDTO;
import jpabook.model.entity.Team;

import java.util.Collection;
import java.util.List;

public class JpqlMain1 {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();
    private static final EntityTransaction transaction = entityManager.getTransaction();

    public static void main(String[] args) {
        transaction.begin();

        try {
            updateMemberAge(26, 25);
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }

    private static void updateMemberAge(int criteriaAge, int changeAge) {
        String query = "update Member m set m.age = :changeAge where m.age = :criteriaAge";
        int result = entityManager.createQuery(query)
                .setParameter("criteriaAge", criteriaAge)
                .setParameter("changeAge", changeAge)
                .executeUpdate();
        System.out.println(result);
    }

    private static void selectByNamedQuery(String username) {
        List<Member> result1 = entityManager.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("name", username)
                .getResultList();
        printMemberList(result1);
    }

    private static void selectByFetchJoin() {
        String query1 = "select m from Member m join fetch m.team";
        List<Member> result1 = entityManager.createQuery(query1, Member.class).getResultList();
        printMemberList(result1);

        String query2 = "select t from Team t join fetch t.members";
        List<Team> result2 = entityManager.createQuery(query2, Team.class).getResultList();
        result2.forEach(System.out::println);
    }

    private static void initialEntityManager() {
        entityManager.flush();
        entityManager.clear();
    }

    // 컬렉션 값 연관 경로 - 묵시적 내부 조인
    private static void selectMembersFromTeam() {
        // Collection 타입으로 반환되기 때문에 t.members.name 같은 호출 불가
        String query = "select t.members from Team t";
        Collection result = entityManager.createQuery(query, Collection.class).getResultList();

        System.out.println(">> selectMembersFromTeam() 결과 출력");
        for (Object o : result) {
            System.out.println(o.toString());
        }
    }

    // 단일 값 연관 경로 - 묵시적 내부 조인
    private static void selectTeamFromMember() {
        String query = "select m.team.name from Member m";
        List<String> result = entityManager.createQuery(query, String.class).getResultList();
        printStringList(result);
    }

    private static void useMyDialect() {
        String query = "select function('group_concat', m.name) from Member m";
        List<String> result = entityManager.createQuery(query, String.class).getResultList();
        printStringList(result);
    }

    private static void useCoalesce() {
        String query = "select coalesce(m.name, '이름 없는 회원') from Member m";
        List<String> result = entityManager.createQuery(query, String.class).getResultList();
        printStringList(result);
    }

    private static void useNullIf() {
        String query = "select NULLIF(m.name, '이름 없는 회원') from Member m";
        List<String> result = entityManager.createQuery(query, String.class).getResultList();
        printStringList(result);
    }

    private static void insert(String username, int age) {
        Team team = new Team();
        team.setName("team1");
        entityManager.persist(team);

        Member member = new Member();
        member.setName(username);
        member.setAge(age);
        member.setTeam(team);
        entityManager.persist(member);
    }

    private static void findById(Long id) {
        TypedQuery<Member> query = entityManager.createQuery("select m from Member m where m.id = :id", Member.class);
        query.setParameter("id", id);
        Member member = query.getSingleResult();
        System.out.println("result = " + member.getName());
    }

    // 반환 타입이 명확
    private static void selectByTypedQuery() {
        TypedQuery<Member> query1 = entityManager.createQuery("select m from Member m", Member.class);
        List<Member> result1 = query1.getResultList();
        printMemberList(result1);

        TypedQuery<String> query2 = entityManager.createQuery("select m.name from Member m", String.class);
        List<String> result2 = query2.getResultList();
        printStringList(result2);
    }

    // 반환 타입이 명확하지 않음
    private static void selectByQuery() {
        List<MemberDTO> resultList = entityManager.createQuery("select new jpabook.model.entity.MemberDTO(m.name, m.age) from Member m", MemberDTO.class)
                .getResultList();

        resultList.forEach(m -> {
            System.out.println(m.getName());
            System.out.println(m.getAge());
        });
    }

    private static void selectWithPaging(int start, int cnt) {
        TypedQuery<Member> query = entityManager.createQuery("select m from Member m", Member.class)
                .setFirstResult(start)
                .setMaxResults(cnt);
        List<Member> result1 = query.getResultList();
        printMemberList(result1);
    }

    private static void printMemberList(List<Member> members) {
        System.out.println(">> start print member list");
        members.forEach(m -> {
            System.out.println(m.getName());
            System.out.println(m.getAge());
        });
    }

    private static void printStringList(List<String> list) {
        System.out.println(">> start print string list");
        list.forEach(System.out::println);
    }
}