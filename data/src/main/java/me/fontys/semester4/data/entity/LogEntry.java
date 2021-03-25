package me.fontys.semester4.data.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "log")
public class LogEntry implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logentryid", nullable = false)
    private Long id;

    @Column(name = "logentrytime")
    private Date logentrytime;

    @Enumerated(EnumType.STRING)
    @Column(name = "logtype")
    private Severity severity;

    @Column(name = "logger")
    private String loggerName;

    @Column(name = "message", length = 1000 )
    private String message;

    protected LogEntry(){
        this(null);
    }

    public LogEntry(String message){
        this(message, Severity.INFO, null);
    }

    public LogEntry(String message, Severity severity, String LoggerName){
        this.message = message;
        this.severity = severity;
        this.loggerName = LoggerName;
        this.logentrytime = new Date();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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
                "logentryid=" + id + '\'' +
                "logentrytime=" + logentrytime + '\'' +
                "message=" + message + '\'' +
                '}';
    }

    public Severity getLogType() {
        return severity;
    }

    public void setLogType(Severity severity) {
        this.severity = severity;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }
}
