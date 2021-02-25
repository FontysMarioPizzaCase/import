package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "order_product_ingredient")
public class OrderProductIngredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ingredientid", nullable = false)
    private Long ingredientid;

    @Column(name = "order_productid")
    private Long orderProductid;

    @Column(name = "name")
    private String name;

    @Column(name = "number")
    private Long number;

    @Column(name = "addprice")
    private String addprice;

    protected OrderProductIngredient() {

    }

    public OrderProductIngredient(Long ingredientid, Long orderProductid, String name, Long number, String addprice) {
        this.ingredientid = ingredientid;
        this.orderProductid = orderProductid;
        this.name = name;
        this.number = number;
        this.addprice = addprice;
    }

    public void setIngredientid(Long ingredientid) {
        this.ingredientid = ingredientid;
    }

    public Long getIngredientid() {
        return ingredientid;
    }

    public void setOrderProductid(Long orderProductid) {
        this.orderProductid = orderProductid;
    }

    public Long getOrderProductid() {
        return orderProductid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public void setAddprice(String addprice) {
        this.addprice = addprice;
    }

    public String getAddprice() {
        return addprice;
    }

    @Override
    public String toString() {
        return "OrderProductIngredient{" +
                "ingredientid=" + ingredientid + '\'' +
                "orderProductid=" + orderProductid + '\'' +
                "name=" + name + '\'' +
                "number=" + number + '\'' +
                "addprice=" + addprice + '\'' +
                '}';
    }
}
