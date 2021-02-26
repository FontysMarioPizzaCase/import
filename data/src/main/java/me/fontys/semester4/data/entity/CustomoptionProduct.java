package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(CustomoptionProduct.CustomoptionProductID.class)
@Table(name = "customoption_product")
public class CustomoptionProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "customoptid")
    private Long customoptid;

    @Id
    @Column(name = "productid")
    private Long productid;

    protected CustomoptionProduct() {

    }

    public CustomoptionProduct(Long customoptid, Long productid) {
        this.customoptid = customoptid;
        this.productid = productid;
    }

    public void setCustomoptid(Long customoptid) {
        this.customoptid = customoptid;
    }

    public Long getCustomoptid() {
        return customoptid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getProductid() {
        return productid;
    }

    @Override
    public String toString() {
        return "CustomoptionProduct{" +
                "customoptid=" + customoptid + '\'' +
                "productid=" + productid + '\'' +
                '}';
    }

    public static class CustomoptionProductID implements Serializable {

        private Long customoptid;
        private Long productid;

        protected CustomoptionProductID() {

        }

        public CustomoptionProductID(Long customoptid, Long productid) {
            this.customoptid = customoptid;
            this.productid = productid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomoptionProductID that = (CustomoptionProductID) o;
            return Objects.equals(customoptid, that.customoptid) && Objects.equals(productid, that.productid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customoptid, productid);
        }
    }
}
