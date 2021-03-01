package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Long productid;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "spicy")
    private Boolean spicy;

    @Column(name = "vegetarian")
    private Boolean vegetarian;

    @Column(name = "deliveryfee")
    private BigDecimal deliveryfee;

    @Column(name = "taxrate")
    private Double taxrate;

    @Column(name = "imagepath")
    private String imagepath;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable( name = "category_product",
            joinColumns = @JoinColumn(name="productid"),
            inverseJoinColumns = @JoinColumn(name="catid"))
    private Set<Category> categories;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable( name = "ingredient_product",
            joinColumns = @JoinColumn(name="productid"),
            inverseJoinColumns = @JoinColumn(name="ingredientid"))
    private Set<Ingredient> ingredients;

    public Product(Long productid,
                   String name,
                   String description,
                   boolean spicy,
                   boolean vegetarian,
                   BigDecimal deliveryfee,
                   Double taxrate,
                   String imagepath) {
        this.productid = productid;
        this.name = name;
        this.description = description;
        this.spicy = spicy;
        this.vegetarian = vegetarian;
        this.deliveryfee = deliveryfee;
        this.taxrate = taxrate;
        this.imagepath = imagepath;
        categories = new HashSet<>();
        ingredients = new HashSet<>();
    }

    protected Product() {
        categories = new HashSet<>();
        ingredients = new HashSet<>();
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getSpicy() {
        return spicy;
    }

    public void setSpicy(boolean spicy) {
        this.spicy = spicy;
    }

    public boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public BigDecimal getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(BigDecimal deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public Double getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(Double taxrate) {
        this.taxrate = taxrate;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productid=" + productid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", spicy='" + spicy + '\'' +
                ", vegetarian='" + vegetarian + '\'' +
                ", deliveryfee='" + deliveryfee + '\'' +
                ", taxrate=" + taxrate +
                ", imagepath='" + imagepath + '\'' +
                ", categories=" + categories +
                ", ingredients=" + ingredients +
                '}';
    }
}
