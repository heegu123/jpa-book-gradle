package jpabook.model.vo;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Zipcode {
    String zip;
    String plusFour;

    public Zipcode() {
    }

    public Zipcode(String zip, String plusFour) {
        this.zip = zip;
        this.plusFour = plusFour;
    }

    public String getZip() {
        return zip;
    }

    public String getPlusFour() {
        return plusFour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zipcode zipcode = (Zipcode) o;
        return Objects.equals(zip, zipcode.zip) && Objects.equals(plusFour, zipcode.plusFour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zip, plusFour);
    }
}
