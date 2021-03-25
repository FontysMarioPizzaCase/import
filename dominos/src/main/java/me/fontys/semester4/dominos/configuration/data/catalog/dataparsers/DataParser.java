package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.LogDetailsEntry;
import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.ValidatorUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.ValidatorUtilFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.*;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.HasProductData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class DataParser<RawT, CleanT> implements HasDatabaseLogger {
    protected final DatabaseLogger<LogEntry> summaryLog;
    private final DatabaseLogger<LogDetailsEntry> detailsLog;
    protected final CleanerUtil cleanerUtil;
    protected final ValidatorUtil validatorUtil;

    public DataParser(DatabaseLoggerFactory databaseLoggerFactory,
                      CleanerUtil cleanerUtil, ValidatorUtilFactory validatorUtilFactory) {
        String className = this.getClass().getName();
        this.summaryLog = databaseLoggerFactory.newDatabaseLogger(className);
        this.detailsLog = databaseLoggerFactory.newDbDetailsLogger(className);
        this.cleanerUtil = cleanerUtil;
        String simpleName = this.getClass().getSimpleName();
        this.validatorUtil = validatorUtilFactory.getValidatorUtil(simpleName);
    }

    public List<CleanT> validateAndClean(List<RawT> rawCsvLines) {
        summaryLog.info(String.format("- Parsing %d lines", rawCsvLines.size()));
        summaryLog.clearReport();

        List<CleanT> cleanedLines = new ArrayList<>();

        for (RawT raw : rawCsvLines) {
            try {
                validate(raw);
                cleanedLines.add(clean(raw));
            } catch (IllegalArgumentException e) {
                summaryLog.addToReport("Skipped (see details)", Severity.WARN);
                detailsLog.addToReport("DETAILS  Skipped: " + raw.toString() + e, Severity.ERROR);
            }
        }
        return cleanedLines;
    }

    protected abstract void validate(RawT line) throws IllegalArgumentException;

    protected abstract CleanT clean(RawT raw);


    public String parseString(String stringProperty, String propertyName, Severity level) {
        // Check empty
        if (stringProperty.isEmpty()) {
            String errorMessage = String.format("Record does not have a %s", propertyName);
            summaryLog.addToReport(errorMessage, level);
            if (level == Severity.ERROR) {
                throw new IllegalArgumentException(errorMessage);
            }
        }

        // Check for unreadable ASCII chars
        String regex = "[\\x20-\\x7E]";
        String negativeRegex = "[^\\x20-\\x7E]";
        String badChars = stringProperty.trim().replaceAll(regex, "");
        if (badChars.length() > 0) {
            if (level == Severity.ERROR) {
                summaryLog.error(String.format("Unreadable character(s) found in %s ", stringProperty));
                throw new IllegalArgumentException(String.format("Unreadable character(s) (%s) found in %s ",
                        badChars, stringProperty));
            }
            String errorMessage = String.format("Unreadable character(s) in %s will be deleted", stringProperty);
            summaryLog.addToReport(errorMessage, level);
            return stringProperty.trim().toLowerCase()
                    .replaceAll(negativeRegex, "");
        }
        return stringProperty;
    }

    public BigDecimal parsePrice(String priceString, String propertyName, Severity level) {
        // check string
        priceString = parseString(priceString, propertyName, level);
        if (priceString.isEmpty()) return null;

        // check comma's
        if (priceString.trim().matches(".*,.*")) {
            summaryLog.addToReport(String.format("Commas in %s will be replaced by periods", propertyName), Severity.INFO);
        }
        priceString = priceString.replace(',', '.');

        // check non-numeric chars
        String regex = "[0-9.]";
        String negativeRegex = "[^0-9.]";
        String badChars = priceString.trim().replaceAll(regex, "");
        if (badChars.length() > 0) {
            if (level == Severity.ERROR) {
                summaryLog.addToReport(String.format("Non-numeric character(s) in %s will be deleted", propertyName), level);
                throw new IllegalArgumentException(String.format("Unexpected character(s) (%s) in %s",
                        badChars, propertyName));
            }
            summaryLog.addToReport(String.format("Non-numeric character(s) in %s", propertyName), level);
        }

        return new BigDecimal(priceString);
    }

    public int parseInteger(String integerString, String propertyName, Severity level) {
        return Integer.parseInt(parseString(integerString, propertyName, level));
    }

    public boolean parseBoolean(String booleanString, String trueValue, String propertyName, Severity level) {
        return parseString(booleanString, propertyName, level).equalsIgnoreCase(trueValue);
    }


    public void validateProductData(HasProductData line) {
        parseString(line.getCategoryName(), "category name", Severity.ERROR);
        validateNotEmpty(line.getSubCategoryName(), "subcategory name", Severity.ERROR);
        validateNotEmpty(line.getProductName(), "product name", Severity.ERROR);
        validateNotEmpty(line.getPrice(), "product price", Severity.ERROR);
        validatePriceFormat(line.getPrice(), "product price", Severity.ERROR);
        validateNotEmpty(line.getProductDescription(), "product description", Severity.WARN);
        validateNotEmpty(line.getIsSpicy(), "product spicy indicator", Severity.WARN);
        validateNotEmpty(line.getIsVegetarian(), "product vegetarian indicator", Severity.WARN);
    }


    public void report() {
        summaryLog.report();
        validatorUtil.report();
    }

    public void reportDetails() {
        detailsLog.report();
    }
}
