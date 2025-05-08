package jpabook.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PhoneServiceProvider {
    @Id
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
