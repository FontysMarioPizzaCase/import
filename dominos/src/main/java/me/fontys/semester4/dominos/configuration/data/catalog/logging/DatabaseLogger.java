package me.fontys.semester4.dominos.configuration.data.catalog.logging;

import me.fontys.semester4.data.entity.ImportLogEntry;
import me.fontys.semester4.data.entity.LogLevel;
import me.fontys.semester4.data.repository.ImportLogEntryRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DatabaseLogger {
    private final Logger logger;
    private final ImportLogEntryRepository logRepository;
    private final Map<Pair<LogLevel, String>, Integer> cachedEntries = new HashMap<>();

    public DatabaseLogger(Logger logger, ImportLogEntryRepository logRepository) {
        this.logger = logger;
        this.logRepository = logRepository;
    }

    public void addToReport(String message, LogLevel logLevel) {
        Pair<LogLevel, String> entry = new MutablePair<>(logLevel, message);
        if (this.cachedEntries.containsKey(entry)) {
            this.cachedEntries.put(entry, this.cachedEntries.get(entry) + 1);
        } else {
            this.cachedEntries.put(entry, 1);
        }
    }

    public void clearReport() {
        this.cachedEntries.clear();
    }

    public void report() {
        int totalEntries = this.cachedEntries.values().stream()
                .reduce(0, Integer::sum);

        if (totalEntries > 0) {
            info(String.format("%s result : %s log entries", getLoggerName(), totalEntries));

            for (Map.Entry<Pair<LogLevel, String>, Integer> entry : this.cachedEntries.entrySet()) {
                LogLevel level = entry.getKey().getKey();
                String message = entry.getKey().getValue();
                int quantity = entry.getValue();
                String finalMessage = String.format("  -> %s : %s", message, quantity);
                if (level == LogLevel.WARN) warn(finalMessage);
                if (level == LogLevel.INFO) info(finalMessage);
                if (level == LogLevel.DEBUG) debug(finalMessage);
                if (level == LogLevel.ERROR) error(finalMessage);
                if (level == LogLevel.TRACE) trace(finalMessage);
            }
        }
    }

    private String getLoggerName() {
        return logger.getName().substring(logger.getName().lastIndexOf('.') + 1);
    }

    public void info(String message){
        logger.info(message);
        logToDb(message, LogLevel.INFO);
    }

    public void warn(String message){
        logger.warn(message);
        logToDb(message, LogLevel.WARN);
    }

    public void debug(String message){
        logger.debug(message);
        logToDb(message, LogLevel.DEBUG);
    }

    public void error(String message){
        logger.error(message);
        logToDb(message, LogLevel.ERROR);
    }

    public void trace(String message){
        logger.trace(message);
        logToDb(message, LogLevel.TRACE);
    }

    private void logToDb(String message, LogLevel logLevel) {
        ImportLogEntry entry = new ImportLogEntry(message, logLevel, getLoggerName());
        logRepository.save(entry);
    }
}
