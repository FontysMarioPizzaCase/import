package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredientid", nullable = false)
    private Long ingredientid;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "size")
    private Integer size;

    @Column(name = "addprice")
    private BigDecimal addprice;

    @Column(name = "available")
    private Boolean isAvailable;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Product> products;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "category_ingredient",
            joinColumns = @JoinColumn(name = "ingredientid"),
            inverseJoinColumns = @JoinColumn(name = "catid"))
    private Set<Category> categories;


    protected Ingredient() {
        this(null, null, null, null, null, true);
    }

    public Ingredient(Long ingredientid, String name, String description, BigDecimal addprice,
                      Integer size, boolean isAvailable) {
        this.ingredientid = ingredientid;
        this.name = name;
        this.description = description;
        this.size = size;
        this.isAvailable = isAvailable;
        this.addprice = addprice;
        products = new HashSet<>();
        categories = new HashSet<>();
    }

    public void setIngredientid(Long ingredientid) {
        this.ingredientid = ingredientid;
    }

    public Long getIngredientid() {
        return ingredientid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Integer getSize() { return size; }

    public void setAddprice(BigDecimal addprice) {
        this.addprice = addprice;
    }

    public BigDecimal getAddprice() {
        return addprice;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Set<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        String idStr = (this.ingredientid != null) ? this.ingredientid.toString() : "null";
        String addPriceStr = (this.addprice != null) ? this.addprice.toString() : "null";
        String sizeStr = (this.size != null) ? this.size.toString() : "n/a";

        return "Ingredient{" +
                "ingredientid=" + idStr + '\'' +
                "name=" + name + '\'' +
                "size=" + sizeStr + '\'' +
                "addprice=" + addPriceStr + '\'' +
                "available=" + isAvailable + '\'' +
                "}";
    }

    public void addCategory(Category value) {
        this.categories.add(value);
    }
}
