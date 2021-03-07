package me.fontys.semester4.data.entity;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "product_price")
@Entity
public class ProductPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priceid", nullable = false)
    private Long priceid;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @Column(name = "price")
    private String price;

    @Column(name = "fromdate")
    private Date fromdate;

    protected ProductPrice() {

    }

    public ProductPrice(Long priceid, Product product, String price, Date fromdate) {
        this.priceid = priceid;
        this.product = product;
        this.price = price;
        this.fromdate = fromdate;
    }

    public void setProductid(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setPriceid(Long priceid) {
        this.priceid = priceid;
    }

    public Long getPriceid() {
        return priceid;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.toString();
    }

    public BigDecimal getPrice() {
        return new BigDecimal(price);
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public Date getFromdate() {
        return fromdate;
    }

    @Override
    public String toString() {
        String productid = (product != null) ? product.getProductid().toString() : "null";

        StringBuilder sb = new StringBuilder();
        sb.append("ProductPrice{");
        sb.append("priceid=").append(priceid);
        sb.append(", productid=").append(productid);
        sb.append(", price='").append(price).append('\'');
        sb.append(", fromdate=").append(fromdate);
        sb.append('}');
        return sb.toString();
    }
}
