package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "customoption_product")
public class CustomoptionProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "customoptid")
    private Long customoptid;

    @Column(name = "productid")
    private Long productid;

    public CustomoptionProduct(Long customoptid, Long productid) {
        this.customoptid = customoptid;
        this.productid = productid;
    }

    public void setCustomoptid(Long customoptid) {
        this.customoptid = customoptid;
    }

    public Long getCustomoptid() {
        return customoptid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getProductid() {
        return productid;
    }

    @Override
    public String toString() {
        return "CustomoptionProduct{" +
                "customoptid=" + customoptid + '\'' +
                "productid=" + productid + '\'' +
                '}';
    }
}
