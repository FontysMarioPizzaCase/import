package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "productid", nullable = false)
    private Long productid;

    @Column(name = "taxrate")
    private Double taxrate;

    @Column(name = "name")
    private String name;

    @Column(name = "imagepath")
    private String imagepath;

    public Product(Long productid, Double taxrate, String name, String imagepath) {
        this.productid = productid;
        this.taxrate = taxrate;
        this.name = name;
        this.imagepath = imagepath;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setTaxrate(Double taxrate) {
        this.taxrate = taxrate;
    }

    public Double getTaxrate() {
        return taxrate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getImagepath() {
        return imagepath;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productid=" + productid + '\'' +
                "taxrate=" + taxrate + '\'' +
                "name=" + name + '\'' +
                "imagepath=" + imagepath + '\'' +
                '}';
    }
}
