package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Table(name = "category_product")
@Entity
public class CategoryProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "productid")
    private Long productid;

    @Column(name = "catid")
    private Long catid;

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
}
