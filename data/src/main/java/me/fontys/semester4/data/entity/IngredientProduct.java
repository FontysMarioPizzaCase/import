package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(IngredientProduct.IngredientProductID.class)
@Table(name = "ingredient_product")
public class IngredientProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ingredientid")
    private Long ingredientid;

    @Id
    @Column(name = "productid")
    private Long productid;

    @Column(name = "defaultnumber")
    private Long defaultnumber;

    protected IngredientProduct() {

    }

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

    public static class IngredientProductID implements Serializable {

        private Long ingredientid;
        private Long productid;

        protected IngredientProductID() {

        }

        public IngredientProductID(Long ingredientid, Long productid) {
            this.ingredientid = ingredientid;
            this.productid = productid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IngredientProductID that = (IngredientProductID) o;
            return Objects.equals(ingredientid, that.ingredientid) && Objects.equals(productid, that.productid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ingredientid, productid);
        }
    }
}
