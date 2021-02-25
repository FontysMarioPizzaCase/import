package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "product_price")
@Entity
public class ProductPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "productid")
    private Long productid;

    @Id
    @Column(name = "priceid", nullable = false)
    private Long priceid;

    @Column(name = "price")
    private String price;

    @Column(name = "fromdate")
    private Date fromdate;

    protected ProductPrice() {

    }

    public ProductPrice(Long productid, Long priceid, String price, Date fromdate) {
        this.productid = productid;
        this.priceid = priceid;
        this.price = price;
        this.fromdate = fromdate;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setPriceid(Long priceid) {
        this.priceid = priceid;
    }

    public Long getPriceid() {
        return priceid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public Date getFromdate() {
        return fromdate;
    }

    @Override
    public String toString() {
        return "ProductPrice{" +
                "productid=" + productid + '\'' +
                "priceid=" + priceid + '\'' +
                "price=" + price + '\'' +
                "fromdate=" + fromdate + '\'' +
                '}';
    }
}
