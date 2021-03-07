package me.fontys.semester4.data.entity;

import org.springframework.transaction.annotation.Transactional;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catid", nullable = false)
    private Long catid;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(referencedColumnName = "catid")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.MERGE})
    private List<Category> children;

    @ManyToMany(mappedBy = "categories", cascade = {CascadeType.MERGE})
    private Set<Product> products;

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
    public String toString() {
        String parentid = (parent != null) ? parent.getCatid().toString() : "null";

        StringBuilder sb = new StringBuilder();
        sb.append("Category{");
        sb.append("catid=").append(catid);
        sb.append(", name='").append(name).append('\'');
        sb.append(", parentid=").append(parentid);
        sb.append("}");

        return sb.toString();
    }
}
