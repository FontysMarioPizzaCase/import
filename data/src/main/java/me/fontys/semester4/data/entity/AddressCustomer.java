package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "address_customer")
public class AddressCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "customerid")
    private Long customerid;

    @Column(name = "addressid")
    private Long addressid;

    public AddressCustomer(Long customerid, Long addressid) {
        this.customerid = customerid;
        this.addressid = addressid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setAddressid(Long addressid) {
        this.addressid = addressid;
    }

    public Long getAddressid() {
        return addressid;
    }

    @Override
    public String toString() {
        return "AddressCustomer{" +
                "customerid=" + customerid + '\'' +
                "addressid=" + addressid + '\'' +
                '}';
    }
}
