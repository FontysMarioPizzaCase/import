package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "ingredient_product")
public class IngredientProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ingredientid")
    private Long ingredientid;

    @Column(name = "productid")
    private Long productid;

    @Column(name = "defaultnumber")
    private Long defaultnumber;

    public IngredientProduct(Long ingredientid, Long productid, Long defaultnumber) {
        this.ingredientid = ingredientid;
        this.productid = productid;
        this.defaultnumber = defaultnumber;
    }

    public void setIngredientid(Long ingredientid) {
        this.ingredientid = ingredientid;
    }

    public Long getIngredientid() {
        return ingredientid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setDefaultnumber(Long defaultnumber) {
        this.defaultnumber = defaultnumber;
    }

    public Long getDefaultnumber() {
        return defaultnumber;
    }

    @Override
    public String toString() {
        return "IngredientProduct{" +
                "ingredientid=" + ingredientid + '\'' +
                "productid=" + productid + '\'' +
                "defaultnumber=" + defaultnumber + '\'' +
                '}';
    }
}
