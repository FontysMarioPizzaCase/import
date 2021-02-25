package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "category_coupon")
public class CategoryCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "couponid")
    private Long couponid;

    @Column(name = "catid")
    private Long catid;

    public CategoryCoupon(Long couponid, Long catid) {
        this.couponid = couponid;
        this.catid = catid;
    }

    public void setCouponid(Long couponid) {
        this.couponid = couponid;
    }

    public Long getCouponid() {
        return couponid;
    }

    public void setCatid(Long catid) {
        this.catid = catid;
    }

    public Long getCatid() {
        return catid;
    }

    @Override
    public String toString() {
        return "CategoryCoupon{" +
                "couponid=" + couponid + '\'' +
                "catid=" + catid + '\'' +
                '}';
    }
}
