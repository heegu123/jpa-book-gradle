package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jpabook.model.entity.Member;

import java.util.List;

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

            Member member1 = new Member();
            member1.setUsername("Kim");
            member1.setAge(26);

            Member member2 = new Member();
            member2.setUsername("Lee");
            member2.setAge(30);

            Member member3 = new Member();
            member3.setUsername("Kim");
            member3.setAge(40);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            tx.commit();

            // 1. JPQL
            String jpql = "select m from Member as m where m.username = 'kim'";
            List<Member> resultList1 = em.createQuery(jpql, Member.class).getResultList();
            System.out.println("1. JPQL - resultList1 = " + resultList1);

            // 2. Criteria Query
            // Criteria 사용 준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query1 = cb.createQuery(Member.class);

            // 루트 클래스 - 조회를 시작할 클래스
            Root<Member> m = query1.from(Member.class);

            //쿼리 생성
            CriteriaQuery<Member> cq = query1.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> resultList2 = em.createQuery(cq).getResultList();
            System.out.println("2. Criteria Query - resultList2 = " + resultList2);

            // 3. Native SQL
            String sql = "SELECT ID, username, age FROM Member WHERE username = 'kim'";
            List<Member> resultList3 = em.createNativeQuery(sql, Member.class).getResultList();
            System.out.println("3. Native SQL - resultList3 = " + resultList3);

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}