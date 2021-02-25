package me.fontys.semester4.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "customoption")
public class Customoption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "customoptid", nullable = false)
    private Long customoptid;

    @Column(name = "name")
    private String name;

    @Column(name = "additionalinfoname")
    private String additionalinfoname;

    @Column(name = "additionalinfovalue")
    private String additionalinfovalue;

    @Column(name = "description")
    private String description;

    @Column(name = "addprice")
    private String addprice;

    protected Customoption() {

    }

    public Customoption(Long customoptid, String name, String additionalinfoname, String additionalinfovalue, String description, String addprice) {
        this.customoptid = customoptid;
        this.name = name;
        this.additionalinfoname = additionalinfoname;
        this.additionalinfovalue = additionalinfovalue;
        this.description = description;
        this.addprice = addprice;
    }

    public void setCustomoptid(Long customoptid) {
        this.customoptid = customoptid;
    }

    public Long getCustomoptid() {
        return customoptid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAdditionalinfoname(String additionalinfoname) {
        this.additionalinfoname = additionalinfoname;
    }

    public String getAdditionalinfoname() {
        return additionalinfoname;
    }

    public void setAdditionalinfovalue(String additionalinfovalue) {
        this.additionalinfovalue = additionalinfovalue;
    }

    public String getAdditionalinfovalue() {
        return additionalinfovalue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAddprice(String addprice) {
        this.addprice = addprice;
    }

    public String getAddprice() {
        return addprice;
    }

    @Override
    public String toString() {
        return "Customoption{" +
                "customoptid=" + customoptid + '\'' +
                "name=" + name + '\'' +
                "additionalinfoname=" + additionalinfoname + '\'' +
                "additionalinfovalue=" + additionalinfovalue + '\'' +
                "description=" + description + '\'' +
                "addprice=" + addprice + '\'' +
                '}';
    }
}
