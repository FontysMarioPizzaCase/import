package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "importlog")
public class ImportLogEntry implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logentryid", nullable = false)
    private Long logentryid;

    @Column(name = "logentrytime")
    private Date logentrytime;

    @Column(name = "message")
    private String message;

    public Long getLogentryid()
    {
        return logentryid;
    }

    public void setLogentryid(Long logentryid)
    {
        this.logentryid = logentryid;
    }

    public Date getLogentrytime()
    {
        return logentrytime;
    }

    public void setLogentrytime(Date logentrytime)
    {
        this.logentrytime = logentrytime;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ImportLogEntry{" +
                "logentryid=" + logentryid + '\'' +
                "logentrytime=" + logentrytime + '\'' +
                "message=" + message + '\'' +
                '}';
    }
}
