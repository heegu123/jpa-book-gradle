package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Child;
import jpabook.model.entity.Parent;

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

            Child child = new Child();
            child.setName("childName");
            em.persist(child);

            Parent parent = new Parent();
            parent.setName("parentName");
            parent.setChild(child);
            em.persist(parent);

            tx.commit();

            Parent findParent = em.find(Parent.class, parent.getId());
            System.out.println(parent.getChild() == child);

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}