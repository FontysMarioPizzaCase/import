package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(CouponOrder.CouponOrderID.class)
@Table(name = "coupon_order")
public class CouponOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "couponid")
    private Long couponid;

    @Id
    @Column(name = "orderid")
    private Long orderid;

    protected CouponOrder() {

    }

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

    public static class CouponOrderID implements Serializable {

        private Long couponid;
        private Long orderid;

        protected CouponOrderID() {

        }

        public CouponOrderID(Long couponid, Long orderid) {
            this.couponid = couponid;
            this.orderid = orderid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CouponOrderID that = (CouponOrderID) o;
            return Objects.equals(couponid, that.couponid) && Objects.equals(orderid, that.orderid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(couponid, orderid);
        }
    }
}
