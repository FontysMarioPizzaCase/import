package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "addressid", nullable = false)
    private Long addressid;

    @Column(name = "streetnr")
    private Long streetnr;

    @Column(name = "streetext")
    private String streetext;

    @Column(name = "postalcodeid")
    private Long postalcodeid;

    protected Address() {

    }

    public Address(Long addressid, Long streetnr, String streetext, Long postalcodeid) {
        this.addressid = addressid;
        this.streetnr = streetnr;
        this.streetext = streetext;
        this.postalcodeid = postalcodeid;
    }

    public void setAddressid(Long addressid) {
        this.addressid = addressid;
    }

    public Long getAddressid() {
        return addressid;
    }

    public void setStreetnr(Long streetnr) {
        this.streetnr = streetnr;
    }

    public Long getStreetnr() {
        return streetnr;
    }

    public void setStreetext(String streetext) {
        this.streetext = streetext;
    }

    public String getStreetext() {
        return streetext;
    }

    public void setPostalcodeid(Long postalcodeid) {
        this.postalcodeid = postalcodeid;
    }

    public Long getPostalcodeid() {
        return postalcodeid;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressid=" + addressid + '\'' +
                "streetnr=" + streetnr + '\'' +
                "streetext=" + streetext + '\'' +
                "postalcodeid=" + postalcodeid + '\'' +
                '}';
    }
}
