package me.fontys.semester4.dominos.configuration.data.catalog.logging;

import me.fontys.semester4.data.entity.ImportLogEntry;
import me.fontys.semester4.data.entity.LogLevel;
import me.fontys.semester4.data.repository.ImportLogEntryRepository;
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
        // resisting the urge to make this a class ;)
        // get totals
        int sumTotal = 0, totalInfo = 0, totalWarnings = 0,
                totalErrors = 0, totalTrace = 0, totalDebug = 0;
        for (Map.Entry<Pair<LogLevel, String>, Integer> entry : this.cachedEntries.entrySet()) {
            LogLevel level = entry.getKey().getKey();
            int quantity = entry.getValue();
            sumTotal += quantity;
            if (level == LogLevel.INFO) totalInfo += quantity;
            if (level == LogLevel.WARN) totalWarnings += quantity;
            if (level == LogLevel.ERROR) totalErrors += quantity;
            if (level == LogLevel.TRACE) totalTrace += quantity;
            if (level == LogLevel.DEBUG) totalDebug += quantity;
        }

        if (sumTotal > 0) {
            // log totals
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s result : ", getLoggerName()));
            if (totalErrors > 0) sb.append(String.format("%d errors ", totalErrors));
            if (totalWarnings > 0) sb.append(String.format("%d warnings ", totalWarnings));
            if (totalInfo > 0) sb.append(String.format("%d messages ", totalInfo));
            if (totalTrace > 0) sb.append(String.format("%d trace ", totalTrace));
            if (totalDebug > 0) sb.append(String.format("%d debug ", totalDebug));
            info(sb.toString());

            // log entries
            for (Map.Entry<Pair<LogLevel, String>, Integer> entry : this.cachedEntries.entrySet()) {
                LogLevel level = entry.getKey().getKey();
                String text = entry.getKey().getValue();
                int quantity = entry.getValue();
                String finalMessage = String.format("  -> %s : %s", text, quantity);
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

    public void info(String message) {
        logger.info(message);
        logToDb(message, LogLevel.INFO);
    }

    public void warn(String message) {
        logger.warn(message);
        logToDb(message, LogLevel.WARN);
    }

    public void debug(String message) {
        logger.debug(message);
        logToDb(message, LogLevel.DEBUG);
    }

    public void error(String message) {
        logger.error(message);
        logToDb(message, LogLevel.ERROR);
    }

    public void trace(String message) {
        logger.trace(message);
        logToDb(message, LogLevel.TRACE);
    }

    private void logToDb(String message, LogLevel logLevel) {
        ImportLogEntry entry = new ImportLogEntry(message, logLevel, getLoggerName());
        logRepository.save(entry);
    }
}
