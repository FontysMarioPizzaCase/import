package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;

@Table(name = "order_product")
@Entity
public class OrderProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_productid", nullable = false)
    private Long orderProductid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid", nullable = false)
    private Product productid;

    @Column(name = "orderid")
    private Long orderid;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private String price;

    @Column(name = "taxrate")
    private Double taxrate;

    @Column(name = "quantity")
    private Long quantity;

    protected OrderProduct() {

    }

    public OrderProduct(Long orderProductid, Product productid, Long orderid, String name, String price, Double taxrate,
                        Long quantity) {
        this.orderProductid = orderProductid;
        this.productid = productid;
        this.orderid = orderid;
        this.name = name;
        this.price = price;
        this.taxrate = taxrate;
        this.quantity = quantity;
    }

    public void setProductid(Product productid) {
        this.productid = productid;
    }

    public Product getProductid() {
        return productid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderProductid(Long orderProductid) {
        this.orderProductid = orderProductid;
    }

    public Long getOrderProductid() {
        return orderProductid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setTaxrate(Double taxrate) {
        this.taxrate = taxrate;
    }

    public Double getTaxrate() {
        return taxrate;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "productid=" + productid + '\'' +
                "orderid=" + orderid + '\'' +
                "orderProductid=" + orderProductid + '\'' +
                "name=" + name + '\'' +
                "price=" + price + '\'' +
                "taxrate=" + taxrate + '\'' +
                "quantity=" + quantity + '\'' +
                '}';
    }
}
