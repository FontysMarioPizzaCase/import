package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Table(name = "product_price")
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
    private String price;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice pp = (ProductPrice) o;
        return product.getName() == pp.product.getName() &&
                price == pp.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, product.getName());
    }

    @Override
    public String toString() {
        String productid = (product != null) ? product.getProductid().toString() : "null";

        return "ProductPrice{" +
                "priceid=" + priceid +
                ", productid=" + productid +
                ", price='" + price + '\'' +
                ", fromdate=" + fromdate +
                '}';
    }
}
