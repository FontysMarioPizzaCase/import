package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "catid", nullable = false)
    private Long catid;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name="catid")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;

    public Category(Long catid, Category parent, String name) {
        this.catid = catid;
        this.parent = parent;
        this.name = name;
        products = new HashSet<>();
    }

    public Category() {

        products = new HashSet<>();
    }

    public void setCatid(Long catid) {
        this.catid = catid;
    }

    public Long getCatid() {
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

    @Override
    public String toString() {
        return "Category{" +
                "catid=" + catid + '\'' +
                "parentid=" + parent.getCatid() + '\'' +
                "name=" + name + '\'' +
                '}';
    }
}
