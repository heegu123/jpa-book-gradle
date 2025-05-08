package jpabook.model.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jpabook.model.entity.PhoneServiceProvider;

import java.util.Objects;

@Embeddable
public class PhoneNumber {
    String areaCode;
    String localNumber;

    @ManyToOne
    PhoneServiceProvider provider;

    public PhoneNumber() {
    }

    public PhoneNumber(String areaCode, String localNumber, PhoneServiceProvider provider) {
        this.areaCode = areaCode;
        this.localNumber = localNumber;
        this.provider = provider;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getLocalNumber() {
        return localNumber;
    }

    public PhoneServiceProvider getProvider() {
        return provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(areaCode, that.areaCode) && Objects.equals(localNumber, that.localNumber) && Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, localNumber, provider);
    }
}
