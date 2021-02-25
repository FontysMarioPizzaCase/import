package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "coupon_order")
public class CouponOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "couponid")
    private Long couponid;

    @Column(name = "orderid")
    private Long orderid;

    public CouponOrder(Long couponid, Long orderid) {
        this.couponid = couponid;
        this.orderid = orderid;
    }

    public void setCouponid(Long couponid) {
        this.couponid = couponid;
    }

    public Long getCouponid() {
        return couponid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public Long getOrderid() {
        return orderid;
    }

    @Override
    public String toString() {
        return "CouponOrder{" +
                "couponid=" + couponid + '\'' +
                "orderid=" + orderid + '\'' +
                '}';
    }
}
