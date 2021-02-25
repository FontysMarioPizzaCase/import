package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "order_custom_option")
public class OrderCustomOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "order_productid")
    private Long orderProductid;

    @Id
    @Column(name = "order_custoptid", nullable = false)
    private Long orderCustoptid;

    @Column(name = "name")
    private String name;

    @Column(name = "additionalinfoname")
    private String additionalinfoname;

    @Column(name = "additionalinfovalue")
    private String additionalinfovalue;

    @Column(name = "description")
    private String description;

    @Column(name = "addprice")
    private String addprice;

    public OrderCustomOption(Long orderProductid, Long orderCustoptid, String name, String additionalinfoname,
                             String additionalinfovalue, String description, String addprice) {
        this.orderProductid = orderProductid;
        this.orderCustoptid = orderCustoptid;
        this.name = name;
        this.additionalinfoname = additionalinfoname;
        this.additionalinfovalue = additionalinfovalue;
        this.description = description;
        this.addprice = addprice;
    }

    public void setOrderProductid(Long orderProductid) {
        this.orderProductid = orderProductid;
    }

    public Long getOrderProductid() {
        return orderProductid;
    }

    public void setOrderCustoptid(Long orderCustoptid) {
        this.orderCustoptid = orderCustoptid;
    }

    public Long getOrderCustoptid() {
        return orderCustoptid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAdditionalinfoname(String additionalinfoname) {
        this.additionalinfoname = additionalinfoname;
    }

    public String getAdditionalinfoname() {
        return additionalinfoname;
    }

    public void setAdditionalinfovalue(String additionalinfovalue) {
        this.additionalinfovalue = additionalinfovalue;
    }

    public String getAdditionalinfovalue() {
        return additionalinfovalue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAddprice(String addprice) {
        this.addprice = addprice;
    }

    public String getAddprice() {
        return addprice;
    }

    @Override
    public String toString() {
        return "OrderCustomOption{" +
                "orderProductid=" + orderProductid + '\'' +
                "orderCustoptid=" + orderCustoptid + '\'' +
                "name=" + name + '\'' +
                "additionalinfoname=" + additionalinfoname + '\'' +
                "additionalinfovalue=" + additionalinfovalue + '\'' +
                "description=" + description + '\'' +
                "addprice=" + addprice + '\'' +
                '}';
    }
}
