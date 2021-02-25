package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Table(name = "store")
@Entity
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "storeid", nullable = false)
    private Long storeid;

    @Column(name = "Name")
    private String name;

    @Column(name = "street")
    private String street;

    public Store(Long storeid, String name, String street) {
        this.storeid = storeid;
        this.name = name;
        this.street = street;
    }

    public void setStoreid(Long storeid) {
        this.storeid = storeid;
    }

    public Long getStoreid() {
        return storeid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeid=" + storeid + '\'' +
                "name=" + name + '\'' +
                "street=" + street + '\'' +
                '}';
    }
}
