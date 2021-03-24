package me.fontys.semester4.data.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "coupon_customer",
            joinColumns = { @JoinColumn(name = "customerid") },
            inverseJoinColumns = { @JoinColumn(name = "couponid") }
    )
    private Set<Coupon> coupons = new HashSet<Coupon>();

    protected Customer() {

    }

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

    public Set<Coupon> getCoupons()
    {
        return coupons;
    }

    public void setCoupons(Set<Coupon> coupons)
    {
        this.coupons = coupons;
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
