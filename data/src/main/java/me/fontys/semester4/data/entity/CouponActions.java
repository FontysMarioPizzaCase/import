package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "coupon_actions")
public class CouponActions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "actionid", nullable = false)
    private Long actionid;

    @Column(name = "d_percentage")
    private Double dPercentage;

    @Column(name = "fixedprice")
    private String fixedprice;

    @Column(name = "freeship")
    private String freeship;

    @Column(name = "each_x_free")
    private Long eachXFree;

    protected CouponActions() {

    }

    public CouponActions(Long actionid, Double dPercentage, String fixedprice, String freeship, Long eachXFree) {
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

    public void setFixedprice(String fixedprice) {
        this.fixedprice = fixedprice;
    }

    public String getFixedprice() {
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
