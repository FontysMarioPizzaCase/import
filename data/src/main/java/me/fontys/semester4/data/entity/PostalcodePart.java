package me.fontys.semester4.data.entity;


import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "postalcode_part")
public class PostalcodePart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postalcodeid", nullable = false)
    private Long postalcodeid;

    @Column(name = "postalcode")
    private String postalcode;

    @Column(name = "starthousenr")
    private Long starthousenr;

    @Column(name = "endhousenr")
    private Long endhousenr;

    @Column(name = "even")
    private String even;

    protected PostalcodePart() {

    }

    public PostalcodePart(Long postalcodeid, String postalcode, Long starthousenr, Long endhousenr, String even) {
        this.postalcodeid = postalcodeid;
        this.postalcode = postalcode;
        this.starthousenr = starthousenr;
        this.endhousenr = endhousenr;
        this.even = even;
    }

    public void setPostalcodeid(Long postalcodeid) {
        this.postalcodeid = postalcodeid;
    }

    public Long getPostalcodeid() {
        return postalcodeid;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setStarthousenr(Long starthousenr) {
        this.starthousenr = starthousenr;
    }

    public Long getStarthousenr() {
        return starthousenr;
    }

    public void setEndhousenr(Long endhousenr) {
        this.endhousenr = endhousenr;
    }

    public Long getEndhousenr() {
        return endhousenr;
    }

    public void setEven(String even) {
        this.even = even;
    }

    public String getEven() {
        return even;
    }

    @Override
    public String toString() {
        return "PostalcodePart{" +
                "postalcodeid=" + postalcodeid + '\'' +
                "postalcode=" + postalcode + '\'' +
                "starthousenr=" + starthousenr + '\'' +
                "endhousenr=" + endhousenr + '\'' +
                "even=" + even + '\'' +
                '}';
    }
}
