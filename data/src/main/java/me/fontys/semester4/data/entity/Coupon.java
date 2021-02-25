package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "couponid", nullable = false)
    private Long couponid;

    @Column(name = "action")
    private Long action;

    @Column(name = "couponcode")
    private String couponcode;

    @Column(name = "starts")
    private Date starts;

    @Column(name = "ends")
    private Date ends;

    @Column(name = "condition")
    private Long condition;

    public Coupon(Long couponid, Long action, String couponcode, Date starts, Date ends, Long condition) {
        this.couponid = couponid;
        this.action = action;
        this.couponcode = couponcode;
        this.starts = starts;
        this.ends = ends;
        this.condition = condition;
    }

    public void setCouponid(Long couponid) {
        this.couponid = couponid;
    }

    public Long getCouponid() {
        return couponid;
    }

    public void setAction(Long action) {
        this.action = action;
    }

    public Long getAction() {
        return action;
    }

    public void setCouponcode(String couponcode) {
        this.couponcode = couponcode;
    }

    public String getCouponcode() {
        return couponcode;
    }

    public void setStarts(Date starts) {
        this.starts = starts;
    }

    public Date getStarts() {
        return starts;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    public Date getEnds() {
        return ends;
    }

    public void setCondition(Long condition) {
        this.condition = condition;
    }

    public Long getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponid=" + couponid + '\'' +
                "action=" + action + '\'' +
                "couponcode=" + couponcode + '\'' +
                "starts=" + starts + '\'' +
                "ends=" + ends + '\'' +
                "condition=" + condition + '\'' +
                '}';
    }
}
