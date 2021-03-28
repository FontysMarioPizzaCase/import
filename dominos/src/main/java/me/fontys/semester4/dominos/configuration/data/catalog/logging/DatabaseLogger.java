package me.fontys.semester4.dominos.configuration.data.catalog.logging;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.data.repository.interfaces.ILogRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.LogEntryFactories.ILogEntryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseLogger<LogEntryType> {
    private final Logger logger;
    protected final ILogRepository<LogEntryType> logRepository;
    private final ILogEntryFactory<LogEntryType> logEntryFactory;
    private final Report report;

    public DatabaseLogger(String loggerName,
                          ILogRepository<LogEntryType> logRepository,
                          ILogEntryFactory<LogEntryType> logEntryFactory,
                          Report report) {
        this.logger = LoggerFactory.getLogger(loggerName);
        this.logRepository = logRepository;
        this.logEntryFactory = logEntryFactory;
        this.report = report;
    }

    public void info(String message) {
        logger.info(message);
        logToDb(message, Severity.INFO);
    }

    public void warn(String message) {
        logger.warn(message);
        logToDb(message, Severity.WARN);
    }

    public void debug(String message) {
        logger.debug(message);
        logToDb(message, Severity.DEBUG);
    }

    public void error(String message) {
        logger.error(message);
        logToDb(message, Severity.ERROR);
    }

    public void trace(String message) {
        logger.trace(message);
        logToDb(message, Severity.TRACE);
    }

    public void log(String message, Severity severity){
        if (severity == Severity.INFO) logger.info(message);
        if (severity == Severity.WARN) logger.warn(message);
        if (severity == Severity.ERROR) logger.error(message);
        if (severity == Severity.DEBUG) logger.debug(message);
        if (severity == Severity.TRACE) logger.trace(message);
        logToDb(message, severity);
    }

    public void logToDb(String message, Severity severity) {
        LogEntryType entry = logEntryFactory.newEntry(message, severity, getLoggerName());
        logRepository.save(entry);
    }

    public String getLoggerName() {
        return logger.getName().substring(logger.getName().lastIndexOf('.') + 1);
    }

    public void addToReport(String message, Severity severity) {
        report.add(message, severity);
    }

    public void clearReport() {
        report.clear();
    }

    public void report() {
        report.log(this);
    }
}
