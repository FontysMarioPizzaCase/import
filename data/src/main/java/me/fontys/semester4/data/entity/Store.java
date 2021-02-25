package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Objects;

@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "storeid")
    private final int storeId;

    @Column(name = "name")
    private final String name;

    @Column(name = "street")
    private final String street;

    protected Store() {
        this(0, null, null);
    }

    public Store(String name, String street) {
        this(0, name, street);
    }

    public Store(int storeId, String name, String street) {
        this.storeId = storeId;
        this.name = name;
        this.street = street;
    }

    public int getStoreId() {
        return this.storeId;
    }

    public String getName() {
        return this.name;
    }

    public String getStreet() {
        return this.street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return storeId == store.storeId && Objects.equals(name, store.name) && Objects.equals(street, store.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, name, street);
    }
}
