package me.fontys.semester4.dominos.configuration.data.catalog.logging;

import me.fontys.semester4.data.entity.Severity;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class Report {
    private Map<Pair<Severity, String>, Integer>  cachedEntries;
    private int totalInfo;
    private int totalWarnings;
    private int totalErrors;
    private int totalTrace;
    private int totalDebug;
    private int sumTotal;

    public Report() {
        clear();
    }

    public void clear() {
        this.cachedEntries = new HashMap<>();
        sumTotal = 0;
        totalInfo = 0;
        totalWarnings = 0;
        totalErrors = 0;
        totalDebug = 0;
        totalTrace = 0;
    }

    public void add(String message, Severity severity) {
        Pair<Severity, String> entry = new MutablePair<>(severity, message);
        if (this.cachedEntries.containsKey(entry)) {
            this.cachedEntries.put(entry, this.cachedEntries.get(entry) + 1);
        } else {
            this.cachedEntries.put(entry, 1);
        }
    }

    public void log(DatabaseLogger databaseLogger) {
        getTotals();
        if (sumTotal > 0) {
            logTotals(databaseLogger);
            logEntries(databaseLogger);
        }
        clear();
    }

    private void getTotals() {
        for (Map.Entry<Pair<Severity, String>, Integer> entry : cachedEntries.entrySet()) {
            Severity level = entry.getKey().getKey();
            int quantity = entry.getValue();
            sumTotal += quantity;
            if (level == Severity.INFO) totalInfo += quantity;
            if (level == Severity.WARN) totalWarnings += quantity;
            if (level == Severity.ERROR) totalErrors += quantity;
            if (level == Severity.TRACE) totalTrace += quantity;
            if (level == Severity.DEBUG) totalDebug += quantity;
        }
    }

    private void logTotals(DatabaseLogger databaseLogger) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Report %s: ", databaseLogger.getLoggerName()));
        if (totalErrors > 0) sb.append(String.format("%d errors ", totalErrors));
        if (totalWarnings > 0) sb.append(String.format("%d warnings ", totalWarnings));
        if (totalInfo > 0) sb.append(String.format("%d messages ", totalInfo));
        if (this.totalTrace > 0) sb.append(String.format("%d trace ", this.totalTrace));
        if (totalDebug > 0) sb.append(String.format("%d debug ", totalDebug));
        databaseLogger.info(sb.toString());
    }

    private void logEntries(DatabaseLogger databaseLogger) {
        for (Map.Entry<Pair<Severity, String>, Integer> entry : cachedEntries.entrySet()) {
            Severity level = entry.getKey().getKey();
            String text = entry.getKey().getValue();
            int quantity = entry.getValue();
            String finalMessage = String.format("  -> %s : %s", text, quantity);
            databaseLogger.log(finalMessage, level);
        }
        // TODO: use saveAll
    }
}