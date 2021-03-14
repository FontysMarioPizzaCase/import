package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "importlog")
public class ImportLogEntry implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logentryid", nullable = false)
    private Long logentryid;

    @Column(name = "logentrytime")
    private Date logentrytime;

    @Column(name = "message")
    private String message;


}
