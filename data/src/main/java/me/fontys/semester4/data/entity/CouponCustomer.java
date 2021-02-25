package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "coupon_customer")
public class CouponCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "couponid")
    private Long couponid;

    @Column(name = "customerid")
    private Long customerid;

    public CouponCustomer(Long couponid, Long customerid) {
        this.couponid = couponid;
        this.customerid = customerid;
    }

    public void setCouponid(Long couponid) {
        this.couponid = couponid;
    }

    public Long getCouponid() {
        return couponid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Long getCustomerid() {
        return customerid;
    }

    @Override
    public String toString() {
        return "CouponCustomer{" +
                "couponid=" + couponid + '\'' +
                "customerid=" + customerid + '\'' +
                '}';
    }
}
