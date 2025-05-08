package jpabook.model.entity;

import jakarta.persistence.*;
import jpabook.model.vo.Address;
import jpabook.model.vo.PhoneNumber;

@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String name;

    @Embedded Address address;
    @Embedded PhoneNumber phoneNumber;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
