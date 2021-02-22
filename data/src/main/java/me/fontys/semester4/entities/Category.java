package me.fontys.semester4.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Category {
    @Id
    private int catid;

    private String name;

    @ManyToOne
    @JoinColumn(name="catid")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;

    public Category(int catid, Category parent, String name) {
        this.catid = catid;
        this.parent = parent;
        this.name = name;
        products = new HashSet<>();
    }

    public Category() {

        products = new HashSet<>();
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public int getCatid() {
        return catid;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }
}
