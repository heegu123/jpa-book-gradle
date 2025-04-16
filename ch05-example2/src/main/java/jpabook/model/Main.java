package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Member;
import jpabook.model.entity.Team;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        /*
        1.	persist(team1) → 영속성 컨텍스트에 등록
        2.	persist(member1), persist(member2) → 영속성 컨텍스트 등록
        3.	createQuery(...) → flush 발생 → insert 쿼리 날림 (DB에 실제 반영은 안 됨, 트랜잭션 안에 있음)
        4.	같은 트랜잭션 안에서의 select이기 때문에 조회 가능
        5.	나중에 tx.commit() 할 때 진짜 디비에 확정 저장
        */
        try{
            tx.begin();
            testSave(em); //저장
//            queryLogicJoin(em); //조회
//            updateRelation(em); //수정
//            queryLogicJoin(em); //조회
//            deleteRelation(em); //연관 관계 제거
            biDirection(em);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        finally {
            em.close();
        }
        emf.close();
    }

    //5.2.1장 예제 5.6
    private static void testSave(EntityManager em){

        //팀1 저장
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        //회원 1 저장
        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1); //회원 -> 팀 참조
        em.persist(member1); //저장

        //회원 2 저장
        Member member2 = new Member("member2", "회원2");
        member2.setTeam(team1);
        em.persist(member2);
    }

    //5.2.2장 예제 5.7
    private static void queryLogicJoin(EntityManager em) {
        String jpql = "select m " +
                "from Member m " +
                "join m.team t " +
                "where t.name =:teamName";

        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName", "팀1")
                .getResultList();

        for (Member member : resultList) {
            System.out.println("[query] member.username= " + member.getUsername());
        }
    }

    //5.2.3장 예제 5.7
    private static void updateRelation(EntityManager em){

        //새로운 팀
        Team team2 = new Team("team2", "팀2");
        em.persist(team2);

        //회원1에 새로운 팀2 설정
        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);
    }

    //5.2.4장 예제 5.8
    private static void deleteRelation(EntityManager em){

        Member member1 = em.find(Member.class, "member1");
        member1.setTeam(null);
    }

    //5.3.2장 예제 5.12
    private static void biDirection(EntityManager em){

        Team team = em.find(Team.class, "team1");
        List<Member> members = team.getMembers(); // 팀 -> 회원
                                                  // 객체 그래프 탐생

        for (Member member : members) {
            System.out.println("member.username = " + member.getUsername());
        }
    }
}
