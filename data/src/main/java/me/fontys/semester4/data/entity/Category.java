package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "catid", nullable = false)
    private Long catid;

    @Column(name = "parentid")
    private Long parentid;

    @Column(name = "name")
    private String name;

    protected Category() {

    }

    public Category(Long catid, Long parentid, String name) {
        this.catid = catid;
        this.parentid = parentid;
        this.name = name;
    }

    public void setCatid(Long catid) {
        this.catid = catid;
    }

    public Long getCatid() {
        return catid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

    public Long getParentid() {
        return parentid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "catid=" + catid + '\'' +
                "parentid=" + parentid + '\'' +
                "name=" + name + '\'' +
                '}';
    }
}
