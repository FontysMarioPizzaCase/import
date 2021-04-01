package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Table(name = "product_price",
        indexes = @Index(name = "productpricedate",
                        columnList = "productid, price, fromdate",
                        unique = true))

@Entity
public class ProductPrice implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priceid", nullable = false)
    private Long priceid;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "fromdate")
    private Date fromdate;

    protected ProductPrice() {

    }

    public ProductPrice(Long priceid, Product product, BigDecimal price, Date fromdate) {
        this.priceid = priceid;
        this.product = product;
        this.setPrice(price);
        this.fromdate = fromdate;
    }

    public void setProduct(Product product) {
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
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public Date getFromdate() {
        return fromdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice that = (ProductPrice) o;
        return product.equals(that.product) && price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, price);
    }

    @Override
    public String toString() {
        String idStr = (priceid != null) ? priceid.toString() : "null";
        String productidStr = (product != null) ? product.getName() : "null";

        return "ProductPrice{" +
                "priceid=" + idStr +
                ", productid=" + productidStr +
                ", price='" + price + '\'' +
                ", fromdate=" + fromdate +
                '}';
    }
}
