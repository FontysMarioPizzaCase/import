package me.fontys.semester4.data.entity;

import javax.persistence.*;

import java.io.Serial;
import java.io.Serializable;
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

    @Column(name = "name")
    private String name;

    @Column(name = "addprice")
    private String addprice;

    @ManyToMany(mappedBy = "ingredients", cascade = {CascadeType.MERGE})
    private Set<Product> products;

    protected Ingredient() {

    }

    public Ingredient(Long ingredientid, String name, String addprice) {
        this.ingredientid = ingredientid;
        this.name = name;
        this.addprice = addprice;
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

    public void setAddprice(String addprice) {
        this.addprice = addprice;
    }

    public String getAddprice() {
        return addprice;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientid=" + ingredientid + '\'' +
                "name=" + name + '\'' +
                "addprice=" + addprice + '\'' +
                '}';
    }
}
