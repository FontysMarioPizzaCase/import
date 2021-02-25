package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(CategoryProduct.CategoryProductID.class)
@Table(name = "category_product")
public class CategoryProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "productid")
    private Long productid;

    @Id
    @Column(name = "catid")
    private Long catid;

    protected CategoryProduct() {

    }

    public CategoryProduct(Long productid, Long catid) {
        this.productid = productid;
        this.catid = catid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setCatid(Long catid) {
        this.catid = catid;
    }

    public Long getCatid() {
        return catid;
    }

    @Override
    public String toString() {
        return "CategoryProduct{" +
                "productid=" + productid + '\'' +
                "catid=" + catid + '\'' +
                '}';
    }

    public static class CategoryProductID implements Serializable {

        private Long productid;
        private Long catid;

        protected CategoryProductID() {

        }

        public CategoryProductID(Long productid, Long catid) {
            this.productid = productid;
            this.catid = catid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CategoryProductID that = (CategoryProductID) o;
            return Objects.equals(productid, that.productid) && Objects.equals(catid, that.catid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productid, catid);
        }
    }
}
