package jpabook.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class Address {

    public Address() {
    }

    public Address(String city, String street, Zipcode zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    @Column(name = "city")
    private String city;
    private String street;
    @Embedded Zipcode zipcode;

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public Zipcode getZipcode() {
        return zipcode;
    }
}
