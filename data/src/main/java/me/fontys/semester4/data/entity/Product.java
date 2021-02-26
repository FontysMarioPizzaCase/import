package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "productid", nullable = false)
    private Long productid;

    @Column(name = "name")
    private String name;

    @Column(name = "taxrate")
    private Double taxrate;

    @Column(name = "imagepath")
    private String imagepath;

    @ManyToMany
    @JoinTable( name = "category_product",
            joinColumns = @JoinColumn(name="productid"),
            inverseJoinColumns = @JoinColumn(name="catid"))
    private Set<Category> categories;

    @ManyToMany
    @JoinTable( name = "ingredient_product",
            joinColumns = @JoinColumn(name="productid"),
            inverseJoinColumns = @JoinColumn(name="ingredientid"))
    private Set<Ingredient> ingredients;

    public Product(Long productid, String name, Double taxrate, String imagepath) {
        this.productid = productid;
        this.taxrate = taxrate;
        this.name = name;
        this.imagepath = imagepath;
        categories = new HashSet<>();
    }

    public Product() {

        categories = new HashSet<>();
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Double getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(Double taxrate) {
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
