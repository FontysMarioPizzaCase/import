package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.LogDetailsEntry;
import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasDatabaseLogger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class DataParser<RawT, CleanT> implements HasDatabaseLogger {
    protected final DatabaseLogger<LogEntry> summaryLog;
    private final DatabaseLogger<LogDetailsEntry> detailsLog;

    public DataParser(DatabaseLoggerFactory databaseLoggerFactory) {
        String className = this.getClass().getName();
        this.summaryLog = databaseLoggerFactory.newDatabaseLogger(className);
        this.detailsLog = databaseLoggerFactory.newDbDetailsLogger(className);
    }

    public List<CleanT> parse(List<RawT> rawCsvLines) {
        summaryLog.info(String.format("- Parsing %d lines", rawCsvLines.size()));
        summaryLog.clearReport();

        List<CleanT> cleanedLines = new ArrayList<>();

        for (RawT raw : rawCsvLines) {
            try {
                CleanT cleaned = parse(raw);
                cleanedLines.add(cleaned);
            } catch (IllegalArgumentException e) {
                summaryLog.addToReport("Skipped (see details)", Severity.ERROR);
                detailsLog.addToReport("DETAILS  Skipped: " + raw.toString() + e, Severity.ERROR);
            }
        }
        return cleanedLines;
    }

    protected abstract CleanT parse(RawT raw) throws IllegalArgumentException;

    public String parseString(String stringProperty, String propertyName, Severity level) {
        // Check empty
        if (stringProperty.isEmpty()) {
            final String MSG = String.format("Record does not have a %s", propertyName);
            summaryLog.addToReport(MSG, level);
            if (level == Severity.ERROR) {
                throw new IllegalArgumentException(MSG);
            }
        }

        // Check for unreadable ASCII chars
        final String READABLE = "[\\x20-\\x7E]";
        final String NONREADABLE = "[^\\x20-\\x7E]";
        String badChars = stringProperty.trim().replaceAll(READABLE, "");
        if (badChars.length() > 0) {
            final String MSG = String.format("Unreadable character(s) in %s will be deleted", propertyName);
            final String ERRORMSG = String.format("Unreadable character(s) found in %s ", propertyName);
            final String DETAILS = String.format("Unreadable character(s) (%s) found in %s ", badChars, stringProperty);

//            if (level == Severity.ERROR) {  TODO: remove
//                summaryLog.addToReport(ERRORMSG, level);
//                throw new IllegalArgumentException(DETAILS);
//            }

            summaryLog.addToReport(MSG, level);
            detailsLog.addToReport(DETAILS, level);

            return stringProperty.trim().toLowerCase()
                    .replaceAll(NONREADABLE, "");
        }
        return stringProperty;
    }

    public BigDecimal parsePrice(String priceString, String propertyName, Severity level) {
        Severity nonBreakingSeverity = Severity.INFO;

        // check string
        priceString = parseString(priceString, propertyName, nonBreakingSeverity);
        if (priceString.isEmpty()) return null;

        // check comma's
        final String COMMA_MSG = String.format("Commas in %s will be replaced by periods", propertyName);
        final String HASCOMMA = ".*,.*";
        if (priceString.trim().matches(HASCOMMA)) {
            summaryLog.addToReport(COMMA_MSG, nonBreakingSeverity);
        }
        priceString = priceString.replace(',', '.');

        // check non-numeric chars
        final String NUMERIC = "[0-9.]";
        final String NONNUMERIC = "[^0-9.]";
        String badChars = priceString.trim().replaceAll(NUMERIC, "");
        if (badChars.length() > 0) {
            final String NUM_MSG = String.format("Non-numeric character(s) in %s will be deleted", propertyName);
            final String ERRORMSG = String.format("Non-numeric character(s) in %s", propertyName);
            final String DETAILS = String.format("Unexpected character(s) (%s) in %s", badChars, priceString);

            if (level == Severity.ERROR) {
                summaryLog.addToReport(ERRORMSG, level);
                throw new IllegalArgumentException(DETAILS);
            }

            summaryLog.addToReport(NUM_MSG, level);
            priceString.trim().replaceAll(NONNUMERIC, "");
        }

        return new BigDecimal(priceString);
    }

    public int parseInteger(String integerString, String propertyName, Severity level) {
        return Integer.parseInt(parseString(integerString, propertyName, level));
    }

    public boolean parseBoolean(String booleanString, String trueValue, String propertyName, Severity level) {
        return parseString(booleanString, propertyName, level).equalsIgnoreCase(trueValue);
    }

    public void report() {
        summaryLog.report();
    }

    public void reportDetails() {
        detailsLog.report();
    }
}
