package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catid", nullable = false)
    private Long catid;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(referencedColumnName = "catid")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Category> children;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;

    @ManyToMany(mappedBy = "categories")
    private Set<Ingredient> ingredients;

    public Category(Long catid, Category parent, String name) {
        this.catid = catid;
        this.parent = parent;
        this.name = name;
        products = new HashSet<>();
    }

    protected Category() {
        this(null, null, null);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        String parentStr = (parent != null) ? parent.getName() : "null";
        String idStr = (this.catid != null) ? this.catid.toString() : "null";

        return "Category{" +
                "catid=" + idStr +
                ", name='" + name + '\'' +
                ", parentid=" + parentStr +
                "}";
    }
}
