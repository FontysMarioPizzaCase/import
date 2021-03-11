package me.fontys.semester4.data.entity;

import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilopooled")
    @GenericGenerator(name = "hilopooled", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "hilo_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "100"),
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled")
            }
    )
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
    private String takeaway;

    @Column(name = "totalprice")
    private String totalprice;

    @Column(name = "deliveryprice")
    private String deliveryprice;

    @Column(name = "applied_discount")
    private String appliedDiscount;

    @Column(name = "streetnr")
    private Long streetnr;

    @Column(name = "customername")
    private String customername;

    protected Order() {

    }

    public Order(Long customer, Long orderid, Store store, Long postalcodeid, Date orderdate, Date deliverydate,
                 String takeaway, String totalprice, String deliveryprice, String appliedDiscount, Long streetnr,
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

    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }

    public String getTakeaway() {
        return takeaway;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setDeliveryprice(String deliveryprice) {
        this.deliveryprice = deliveryprice;
    }

    public String getDeliveryprice() {
        return deliveryprice;
    }

    public void setAppliedDiscount(String appliedDiscount) {
        this.appliedDiscount = appliedDiscount;
    }

    public String getAppliedDiscount() {
        return appliedDiscount;
    }

    public void setStreetnr(Long streetnr) {
        this.streetnr = streetnr;
    }

    public Long getStreetnr() {
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
