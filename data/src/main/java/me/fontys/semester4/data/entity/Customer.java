package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    public Customer(Long customerid, String email, String name, String lastname) {
        this.customerid = customerid;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerid=" + customerid + '\'' +
                "email=" + email + '\'' +
                "name=" + name + '\'' +
                "lastname=" + lastname + '\'' +
                '}';
    }
}
