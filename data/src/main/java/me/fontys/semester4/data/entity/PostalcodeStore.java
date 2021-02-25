package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "postalcode_store")
public class PostalcodeStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "storeid")
    private Long storeid;

    @Column(name = "postalcodeid")
    private Long postalcodeid;

    public PostalcodeStore(Long storeid, Long postalcodeid) {
        this.storeid = storeid;
        this.postalcodeid = postalcodeid;
    }

    public void setStoreid(Long storeid) {
        this.storeid = storeid;
    }

    public Long getStoreid() {
        return storeid;
    }

    public void setPostalcodeid(Long postalcodeid) {
        this.postalcodeid = postalcodeid;
    }

    public Long getPostalcodeid() {
        return postalcodeid;
    }

    @Override
    public String toString() {
        return "PostalcodeStore{" +
                "storeid=" + storeid + '\'' +
                "postalcodeid=" + postalcodeid + '\'' +
                '}';
    }
}
