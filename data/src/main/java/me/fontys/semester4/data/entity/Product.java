package me.fontys.semester4.data.entity;

import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Long productid;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "spicy")
    private Boolean spicy;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "vegetarian")
    private Boolean vegetarian;

    @Column(name = "taxrate")
    private Double taxrate;

    @Column(name = "imagepath")
    private String imagepath;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "category_product",
            joinColumns = @JoinColumn(name = "productid"),
            inverseJoinColumns = @JoinColumn(name = "catid"))
    private Set<Category> categories;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "ingredient_product",
            joinColumns = @JoinColumn(name = "productid"),
            inverseJoinColumns = @JoinColumn(name = "ingredientid"))
    private Set<Ingredient> ingredients;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ProductPrice> prices;

    public Product(Long productid, String name, String description, Boolean spicy,
                   Boolean vegetarian, Boolean available, Double taxrate,
                   String imagepath) {
        this.productid = productid;
        this.name = name;
        this.description = description;
        this.spicy = spicy;
        this.vegetarian = vegetarian;
        this.available = available;
        this.taxrate = taxrate;
        this.imagepath = imagepath;
        categories = new HashSet<>();
        ingredients = new HashSet<>();
        prices = new ArrayList<>();
    }

    protected Product() {
        this(null, null, null, null, null,
                null, null, null);
    }

    public Long getProductid() {
        return productid;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Boolean getSpicy() {
        return spicy;
    }
    public Boolean getVegetarian() {
        return vegetarian;
    }
    public Boolean getAvailable() {
        return available;
    }
    public Double getTaxrate() {
        return taxrate;
    }
    public String getImagepath() {
        return imagepath;
    }
    public Set<Category> getCategories() {
        return categories;
    }
    public Set<Ingredient> getIngredients() {
        return ingredients;
    }
    public List<ProductPrice> getPrices() {
        return prices;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSpicy(boolean spicy) {
        this.spicy = spicy;
    }
    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public void setTaxrate(Double taxrate) {
        this.taxrate = taxrate;
    }
    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public void setPrices(List<ProductPrice> prices) {
        this.prices = prices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        String idStr = (productid != null) ? productid.toString() : "null";

        return "Product{" +
                "productid=" + idStr +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", spicy='" + spicy + '\'' +
                ", vegetarian='" + vegetarian + '\'' +
                ", available='" + available + '\'' +
                ", taxrate=" + taxrate +
                ", imagepath='" + imagepath + '\'' +
                "}";
    }


    public void addIngredient(Ingredient value) {
        this.ingredients.add(value);
    }

    public void addCategory(Category value) {
        this.categories.add(value);
    }
}
