package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Parent;
import jpabook.model.entity.ParentId;

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

            Parent parent = new Parent();
            ParentId parentId = new ParentId("myId1", "myId2");
            parent.setId(parentId);
            parent.setName("parentName");
            em.persist(parent);

            tx.commit();

            Parent findParent = em.find(Parent.class, parentId);
            System.out.println(findParent == parent);

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}