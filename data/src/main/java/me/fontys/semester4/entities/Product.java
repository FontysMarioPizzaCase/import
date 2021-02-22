package me.fontys.semester4.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {
    @Column(name="productid")
    @Id
    private int productid;

    private double taxrate;

    private String name;

    private String imagepath;

    @ManyToMany
    @JoinTable( name = "category_product",
                joinColumns = @JoinColumn(name="productid"),
                inverseJoinColumns = @JoinColumn(name="catid"))
    private Set<Category> categories;


    public Product(int productid, double taxrate, String name, String imagepath) {
        this.productid = productid;
        this.taxrate = taxrate;
        this.name = name;
        this.imagepath = imagepath;
        categories = new HashSet<>();
    }

    public Product() {

        categories = new HashSet<>();
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public double getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(double taxrate) {
        this.taxrate = taxrate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
