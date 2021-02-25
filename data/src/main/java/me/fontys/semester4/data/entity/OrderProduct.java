package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Table(name = "order_product")
@Entity
public class OrderProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "productid")
    private Long productid;

    @Column(name = "orderid")
    private Long orderid;

    @Id
    @Column(name = "order_productid", nullable = false)
    private Long orderProductid;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private String price;

    @Column(name = "taxrate")
    private Double taxrate;

    @Column(name = "quantity")
    private Long quantity;

    public OrderProduct(Long productid, Long orderid, Long orderProductid, String name, String price, Double taxrate,
                        Long quantity) {
        this.productid = productid;
        this.orderid = orderid;
        this.orderProductid = orderProductid;
        this.name = name;
        this.price = price;
        this.taxrate = taxrate;
        this.quantity = quantity;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getProductid() {
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
