package me.fontys.semester4.tempdata.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "postalcode_import")
@NamedStoredProcedureQuery(
        name = "processPostalcodes",
        procedureName = "process_postalcodes"
        )

public class PostalcodeTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilopooled")
    @GenericGenerator(name = "hilopooled", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "hilo_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "100"),
                    @Parameter(name = "optimizer", value = "pooled")
            }
    )
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

    @Column(name = "municipality")
    private Long municipality;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    protected PostalcodeTemp() {

    }

    public PostalcodeTemp(Long postalcodeid,
                          String postalcode,
                          Long starthousenr,
                          Long endhousenr,
                          String even,
                          Long municipality,
                          String street,
                          String city) {
        this.postalcodeid = postalcodeid;
        this.postalcode = postalcode;
        this.starthousenr = starthousenr;
        this.endhousenr = endhousenr;
        this.even = even;
        this.municipality = municipality;
        this.street = street;
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
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
                "municipality=" + municipality + '\'' +
                "street=" + street + '\'' +
                "city=" + city + '\'' +
                '}';
    }

    public Long getMunicipality()
    {
        return municipality;
    }

    public void setMunicipality(Long municipality)
    {
        this.municipality = municipality;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }
}
