package me.fontys.semester4.data.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "couponid", nullable = false)
    private Long couponid;

    @ManyToOne
    @JoinColumn(name = "action", nullable = true)
    private CouponActions action;

    @Column(name = "couponcode")
    private String couponcode;

    @Column(name = "starts")
    private Date starts;

    @Column(name = "ends")
    private Date ends;

    @ManyToOne
    @JoinColumn(name = "condition", nullable = true)
    private CouponConditions condition;

    @ManyToMany(mappedBy = "coupons")
    private Set<Customer> employees = new HashSet<>();

    protected Coupon() {

    }

    public Coupon(Long couponid, CouponActions action, String couponcode, Date starts, Date ends, CouponConditions condition) {
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

    public void setAction(CouponActions action) {
        this.action = action;
    }

    public CouponActions getAction() {
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

    public void setCondition(CouponConditions condition) {
        this.condition = condition;
    }

    public CouponConditions getCondition() {
        return condition;
    }

    public Set<Customer> getEmployees()
    {
        return employees;
    }

    public void setEmployees(Set<Customer> employees)
    {
        this.employees = employees;
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
