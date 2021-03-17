package me.fontys.semester4.tempdata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "order_import")
public class OrderTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "storeName")
    private String storeName;

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "phoneNumber")
    private int phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "orderDate")
    private Date orderDate;

    @Column(name = "deliveryType")
    private String deliveryType;

    @Column(name = "deliveryTime")
    private Date deliveryTime;

    @Column(name = "product")
    private String product;

    @Column(name = "pizzaDough")
    private String pizzaDough;

    @Column(name = "pizzaSauce")
    private String pizzaSauce;

    @Column(name = "productPrice")
    private BigDecimal productPrice;

    @Column(name = "deliveryCost")
    private BigDecimal deliveryCost;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "extraIngredients")
    private String extraIngredients;

    @Column(name = "extraIngredientPrice")
    private BigDecimal extraIngredientPrice;

    @Column(name = "orderLinePrice")
    private BigDecimal orderLinePrice;

    @Column(name = "totalPrice")
    private BigDecimal totalPrice;

    @Column(name = "usedCoupon")
    private String usedCoupon;

    @Column(name = "couponDiscount")
    private BigDecimal couponDiscount;

    @Column(name = "totalPriceAltered")
    private BigDecimal totalPriceAltered;

    public OrderTemp(String storeName, String customerName, int phoneNumber, String email, String address, String city,
                     Date orderDate, String deliveryType, Date deliveryTime, String product, String pizzaDough,
                     String pizzaSauce, BigDecimal productPrice, BigDecimal deliveryCost, int quantity, String extraIngredients,
                     BigDecimal extraIngredientPrice, BigDecimal orderLinePrice, BigDecimal totalPrice,
                     String usedCoupon, BigDecimal couponDiscount, BigDecimal totalPriceAltered) {
        this.storeName = storeName;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.city = city;
        this.orderDate = orderDate;
        this.deliveryType = deliveryType;
        this.deliveryTime = deliveryTime;
        this.product = product;
        this.pizzaDough = pizzaDough;
        this.pizzaSauce = pizzaSauce;
        this.productPrice = productPrice;
        this.deliveryCost = deliveryCost;
        this.quantity = quantity;
        this.extraIngredients = extraIngredients;
        this.extraIngredientPrice = extraIngredientPrice;
        this.orderLinePrice = orderLinePrice;
        this.totalPrice = totalPrice;
        this.usedCoupon = usedCoupon;
        this.couponDiscount = couponDiscount;
        this.totalPriceAltered = totalPriceAltered;
    }
}
