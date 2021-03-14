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

    @Column(name = "addprice")
    private String addprice;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Product> products;

    protected Ingredient() {
        this(null, null, null);
    }

    public Ingredient(Long ingredientid, String name, BigDecimal addprice) {
        this.ingredientid = ingredientid;
        this.name = name;
        if (addprice != null) setAddprice(addprice);
        products = new HashSet<>();
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

    public void setAddprice(BigDecimal addprice) {
        this.addprice = addprice.toString();
    }

    public BigDecimal getAddprice() {
        if (addprice != null) return new BigDecimal(addprice);
        return null;
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
        String addPriceStr = (this.addprice != null) ? this.addprice : "null";

        return "Ingredient{" +
                "ingredientid=" + idStr + '\'' +
                "name=" + name + '\'' +
                "addprice=" + addPriceStr + '\'' +
                "}";
    }
}
