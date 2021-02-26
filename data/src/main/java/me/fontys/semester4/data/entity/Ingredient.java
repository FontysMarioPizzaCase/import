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
    @Column(name = "ingredientid", nullable = false)
    private Long ingredientid;

    @Column(name = "name")
    private String name;

    @Column(name = "addprice")
    private String addprice;

    @Column(name = "spicy")
    private String spicy;

    @Column(name = "vegetarian")
    private String vegetarian;

    @Column(name = "deliveryfee")
    private String deliveryfee;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Product> products;

    protected Ingredient() {

    }

    public Ingredient(Long ingredientid, String name, String addprice, String spicy, String vegetarian,
                      String deliveryfee) {
        this.ingredientid = ingredientid;
        this.name = name;
        this.addprice = addprice;
        this.spicy = spicy;
        this.vegetarian = vegetarian;
        this.deliveryfee = deliveryfee;
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

    public void setSpicy(String spicy) {
        this.spicy = spicy;
    }

    public String getSpicy() {
        return spicy;
    }

    public void setVegetarian(String vegetarian) {
        this.vegetarian = vegetarian;
    }

    public String getVegetarian() {
        return vegetarian;
    }

    public void setDeliveryfee(String deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public String getDeliveryfee() {
        return deliveryfee;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientid=" + ingredientid + '\'' +
                "name=" + name + '\'' +
                "addprice=" + addprice + '\'' +
                "spicy=" + spicy + '\'' +
                "vegetarian=" + vegetarian + '\'' +
                "deliveryfee=" + deliveryfee + '\'' +
                '}';
    }
}
