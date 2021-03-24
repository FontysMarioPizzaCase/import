package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "`order`") // https://stackoverflow.com/questions/3599803/jpa-hibernate-cant-create-entity-called-order
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "customer")
    private Long customer;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", nullable = false)
    private Long orderid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store", nullable = false)
    private Store store;

    @Column(name = "postalcodeid")
    private Long postalcodeid;

    @Column(name = "orderdate")
    private Date orderdate;

    @Column(name = "deliverydate")
    private Date deliverydate;

    @Column(name = "takeaway")
    private boolean takeaway;

    @Column(name = "totalprice")
    private Float totalprice;

    @Column(name = "deliveryprice")
    private Float deliveryprice;

    @Column(name = "applied_discount")
    private String appliedDiscount;

    @Column(name = "streetnr")
    private String streetnr;

    @Column(name = "customername")
    private String customername;

    protected Order() {

    }

    public Order(Long customer, Long orderid, Store store, Long postalcodeid, Date orderdate, Date deliverydate,
                 boolean takeaway, Float totalprice, Float deliveryprice, String appliedDiscount, String streetnr,
                 String customername) {
        this.customer = customer;
        this.orderid = orderid;
        this.store = store;
        this.postalcodeid = postalcodeid;
        this.orderdate = orderdate;
        this.deliverydate = deliverydate;
        this.takeaway = takeaway;
        this.totalprice = totalprice;
        this.deliveryprice = deliveryprice;
        this.appliedDiscount = appliedDiscount;
        this.streetnr = streetnr;
        this.customername = customername;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public void setPostalcodeid(Long postalcodeid) {
        this.postalcodeid = postalcodeid;
    }

    public Long getPostalcodeid() {
        return postalcodeid;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setDeliverydate(Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    public Date getDeliverydate() {
        return deliverydate;
    }

    public void setTakeaway(boolean takeaway) {
        this.takeaway = takeaway;
    }

    public boolean getTakeaway() {
        return takeaway;
    }

    public void setTotalprice(float totalprice) {
        this.totalprice = totalprice;
    }

    public float getTotalprice() {
        return totalprice;
    }

    public void setDeliveryprice(float deliveryprice) {
        this.deliveryprice = deliveryprice;
    }

    public float getDeliveryprice() {
        return deliveryprice;
    }

    public void setAppliedDiscount(String appliedDiscount) {
        this.appliedDiscount = appliedDiscount;
    }

    public String getAppliedDiscount() {
        return appliedDiscount;
    }

    public void setStreetnr(String streetnr) {
        this.streetnr = streetnr;
    }

    public String getStreetnr() {
        return streetnr;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomername() {
        return customername;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer + '\'' +
                "orderid=" + orderid + '\'' +
                "store=" + store + '\'' +
                "postalcodeid=" + postalcodeid + '\'' +
                "orderdate=" + orderdate + '\'' +
                "deliverydate=" + deliverydate + '\'' +
                "takeaway=" + takeaway + '\'' +
                "totalprice=" + totalprice + '\'' +
                "deliveryprice=" + deliveryprice + '\'' +
                "appliedDiscount=" + appliedDiscount + '\'' +
                "streetnr=" + streetnr + '\'' +
                "customername=" + customername + '\'' +
                '}';
    }
}
