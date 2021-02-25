package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "coupon_conditions")
public class CouponConditions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "conditionid", nullable = false)
    private Long conditionid;

    @Column(name = "min_price")
    private String minPrice;

    @Column(name = "takeaway")
    private String takeaway;

    @Column(name = "min_quantity")
    private Long minQuantity;

    protected CouponConditions() {

    }

    public CouponConditions(Long conditionid, String minPrice, String takeaway, Long minQuantity) {
        this.conditionid = conditionid;
        this.minPrice = minPrice;
        this.takeaway = takeaway;
        this.minQuantity = minQuantity;
    }

    public void setConditionid(Long conditionid) {
        this.conditionid = conditionid;
    }

    public Long getConditionid() {
        return conditionid;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }

    public String getTakeaway() {
        return takeaway;
    }

    public void setMinQuantity(Long minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Long getMinQuantity() {
        return minQuantity;
    }

    @Override
    public String toString() {
        return "CouponConditions{" +
                "conditionid=" + conditionid + '\'' +
                "minPrice=" + minPrice + '\'' +
                "takeaway=" + takeaway + '\'' +
                "minQuantity=" + minQuantity + '\'' +
                '}';
    }
}
