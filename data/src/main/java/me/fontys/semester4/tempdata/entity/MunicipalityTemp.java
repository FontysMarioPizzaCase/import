package me.fontys.semester4.tempdata.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "municipality_import")
public class MunicipalityTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "municipalityid", nullable = false)
    private Long municipalityId;

    @Column(name = "name")
    private String name;

    protected MunicipalityTemp() {

    }

    public MunicipalityTemp(Long municipalityId,
                            String name) {
        this.municipalityId = municipalityId;
        this.name = name;
    }



    @Override
    public String toString() {
        return "PostalcodePart{" +
                "muncipalityid=" + municipalityId + '\'' +
                "name=" + name + '\'' +
                '}';
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getMunicipalityId()
    {
        return municipalityId;
    }

    public void setMunicipalityId(Long municipalityId)
    {
        this.municipalityId = municipalityId;
    }
}
