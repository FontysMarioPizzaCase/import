package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(CategoryCoupon.CategoryCouponID.class)
@Table(name = "category_coupon")
public class CategoryCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "couponid")
    private Long couponid;

    @Id
    @Column(name = "catid")
    private Long catid;

    protected CategoryCoupon() {

    }

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

    public static class CategoryCouponID implements Serializable {

        private Long couponid;
        private Long catid;

        protected CategoryCouponID() {

        }

        public CategoryCouponID(Long couponid, Long catid) {
            this.couponid = couponid;
            this.catid = catid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CategoryCouponID that = (CategoryCouponID) o;
            return Objects.equals(couponid, that.couponid) && Objects.equals(catid, that.catid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(couponid, catid);
        }
    }
}
