package me.fontys.semester4.data.entity;

import javax.persistence.*;

import java.io.Serializable;

@Table(name = "store")
@Entity
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storeid", nullable = false)
    private Long storeid;

    @Column(name = "Name")
    private String name;

    @Column(name = "street")
    private String street;

    @Column(name = "municipality")
    private String municipality;

    protected Store() {

    }

    public Store(Long storeid, String name, String street, String municipality) {
        this.storeid = storeid;
        this.name = name;
        this.street = street;
        this.municipality = municipality;
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
                "municipality=" + municipality + '\'' +
                '}';
    }

    public String getMunicipality()
    {
        return municipality;
    }

    public void setMunicipality(String municipality)
    {
        this.municipality = municipality;
    }
}
