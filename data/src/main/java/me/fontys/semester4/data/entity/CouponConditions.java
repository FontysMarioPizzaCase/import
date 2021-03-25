package me.fontys.semester4.data.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "coupon_conditions")
public class CouponConditions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conditionid", nullable = false)
    private Long conditionid;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Column(name = "takeaway")
    private boolean takeaway;

    @Column(name = "min_quantity")
    private Long minQuantity;

    protected CouponConditions() {

    }

    public CouponConditions(Long conditionid, BigDecimal minPrice, boolean takeaway, Long minQuantity) {
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

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setTakeaway(boolean takeaway) {
        this.takeaway = takeaway;
    }

    public boolean getTakeaway() {
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
