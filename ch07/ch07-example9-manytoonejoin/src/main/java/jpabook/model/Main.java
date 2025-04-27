package jpabook.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.model.entity.Child;
import jpabook.model.entity.Parent;

import java.util.List;
import java.util.Objects;

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

            Child child1 = new Child();
            child1.setName("childName1");
            em.persist(child1);

            Child child2 = new Child();
            child2.setName("childName2");
            em.persist(child2);

            Parent parent = new Parent();
            parent.setName("parentName");
            parent.addChild(child1);
            parent.addChild(child2);
            em.persist(parent);

            tx.commit();

            List<Child> listA = parent.getChild();
            List<Child> listB = List.of(child1, child2);

            boolean allMatch = true;
            for (int i = 0; i < listA.size(); i++) {
                if (!Objects.equals(listA.get(i).getId(), listB.get(i).getId())) {
                    allMatch = false;
                    break;
                }
            }
            System.out.println(allMatch);

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}