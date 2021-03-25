package me.fontys.semester4.data.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Table(name = "coupon_actions")
public class CouponActions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actionid", nullable = false)
    private Long actionid;

    @Column(name = "d_percentage")
    private Double dPercentage;

    @Column(name = "fixedprice")
    private BigDecimal fixedprice;

    @Column(name = "freeship")
    private String freeship;

    @Column(name = "each_x_free")
    private Long eachXFree;

    protected CouponActions() {

    }

    public CouponActions(Long actionid, Double dPercentage, BigDecimal fixedprice, String freeship, Long eachXFree) {
        this.actionid = actionid;
        this.dPercentage = dPercentage;
        this.fixedprice = fixedprice;
        this.freeship = freeship;
        this.eachXFree = eachXFree;
    }

    public void setActionid(Long actionid) {
        this.actionid = actionid;
    }

    public Long getActionid() {
        return actionid;
    }

    public void setDPercentage(Double dPercentage) {
        this.dPercentage = dPercentage;
    }

    public Double getDPercentage() {
        return dPercentage;
    }

    public void setFixedprice(BigDecimal fixedprice) {
        this.fixedprice = fixedprice;
    }

    public BigDecimal getFixedprice() {
        return fixedprice;
    }

    public void setFreeship(String freeship) {
        this.freeship = freeship;
    }

    public String getFreeship() {
        return freeship;
    }

    public void setEachXFree(Long eachXFree) {
        this.eachXFree = eachXFree;
    }

    public Long getEachXFree() {
        return eachXFree;
    }

    @Override
    public String toString() {
        return "CouponActions{" +
                "actionid=" + actionid + '\'' +
                "dPercentage=" + dPercentage + '\'' +
                "fixedprice=" + fixedprice + '\'' +
                "freeship=" + freeship + '\'' +
                "eachXFree=" + eachXFree + '\'' +
                '}';
    }
}
