package jpabook.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id
    @GeneratedValue
    @Column(name= "PARENT_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent")
    private List<Child> child = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    public void addChild(Child child) {
        if (this.child.contains(child)) return;
        this.child.add(child);
        // 이전 parent 있으면 떼어내기
        if (child.getParent() != null) {
            child.getParent().getChild().remove(child);
        }
        child.setParent(this);
    }
}
