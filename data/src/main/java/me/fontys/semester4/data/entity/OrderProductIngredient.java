package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "order_product_ingredient")
public class OrderProductIngredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_ingredientid")
    private Long orderProductIngredientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_productid", nullable = false)
    private OrderProduct orderProductid;

    @Column(name = "ingredientid", nullable = false)
    private Long ingredientid;

    @Column(name = "name")
    private String name;

    @Column(name = "number")
    private Long number;

    @Column(name = "addprice")
    private String addprice;

    protected OrderProductIngredient() {

    }

    public OrderProductIngredient(Long orderProductIngredientId, OrderProduct orderProductid, Long ingredientid, String name, Long number, String addprice) {
        this.orderProductIngredientId = orderProductIngredientId;
        this.orderProductid = orderProductid;
        this.ingredientid = ingredientid;
        this.name = name;
        this.number = number;
        this.addprice = addprice;
    }

    public Long getOrderProductIngredientId() {
        return this.orderProductIngredientId;
    }

    public void setOrderProductIngredientId(Long orderProductIngredientId) {
        this.orderProductIngredientId = orderProductIngredientId;
    }

    public void setIngredientid(Long ingredientid) {
        this.ingredientid = ingredientid;
    }

    public Long getIngredientid() {
        return ingredientid;
    }

    public void setOrderProductid(OrderProduct orderProductid) {
        this.orderProductid = orderProductid;
    }

    public OrderProduct getOrderProductid() {
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
