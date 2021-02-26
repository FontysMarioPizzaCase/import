package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(CouponCustomer.CouponCustomerID.class)
@Table(name = "coupon_customer")
public class CouponCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "couponid")
    private Long couponid;

    @Id
    @Column(name = "customerid")
    private Long customerid;

    protected CouponCustomer() {

    }

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

    public static class CouponCustomerID implements Serializable {

        private Long couponid;
        private Long customerid;

        protected CouponCustomerID() {

        }

        public CouponCustomerID(Long couponid, Long customerid) {
            this.couponid = couponid;
            this.customerid = customerid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CouponCustomerID that = (CouponCustomerID) o;
            return Objects.equals(couponid, that.couponid) && Objects.equals(customerid, that.customerid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(couponid, customerid);
        }
    }
}
