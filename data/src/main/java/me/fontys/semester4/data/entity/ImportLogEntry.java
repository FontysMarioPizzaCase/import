package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "importlog")
public class ImportLogEntry implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logentryid", nullable = false)
    private Long logentryid;

    @Column(name = "logentrytime")
    private Date logentrytime;

    @Enumerated(EnumType.STRING)
    @Column(name = "logtype")
    private LogLevel logLevel;

    @Column(name = "logger")
    private String loggerName;

    @Column(name = "message")
    private String message;

    protected ImportLogEntry(){
        this(null);
    }

    public ImportLogEntry(String message){
        this(message, LogLevel.INFO, null);
    }

    public ImportLogEntry(String message, LogLevel logLevel, String LoggerName){
        this.message = message;
        this.logLevel = logLevel;
        this.loggerName = LoggerName;
        this.logentrytime = new Date();
    }

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

    public LogLevel getLogType() {
        return logLevel;
    }

    public void setLogType(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }
}
