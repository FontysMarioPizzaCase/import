package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasDatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.HasProductData;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DataParser<RawT, CleanT> implements HasDatabaseLogger {
    protected final DatabaseLogger<LogEntry> log;

    public DataParser(DatabaseLoggerFactory databaseLoggerFactory) {
        String className = this.getClass().getName();
        this.log = databaseLoggerFactory.newDatabaseLogger(className);
    }

    public List<CleanT> parse(List<RawT> rawCsvLines) {
        log.info(String.format("- Parsing %d lines", rawCsvLines.size()));
        log.clearReport();

        List<CleanT> cleanedLines = new ArrayList<>();

        for (RawT raw : rawCsvLines) {
            try {
                CleanT cleaned = parse(raw);
                cleanedLines.add(cleaned);
            } catch (IllegalArgumentException e) {
                log.addToReport("SKIPPED: " + raw.toString() + e, Severity.ERROR);
            }
        }
        return cleanedLines;
    }

    protected abstract CleanT parse(RawT raw) throws IllegalArgumentException;

    public String parseString(String stringProperty, String propertyName, Severity level) {

        // Check empty
        if (stringProperty.isEmpty()) {
            final String MSG = String.format("Field %s is empty", propertyName);
            log.addToReport(MSG, level);
            if (level == Severity.ERROR) {
                throw new IllegalArgumentException(MSG);
            }
        }

        // Check for unreadable ASCII chars
        final Severity UNREADABLECHARSEVERITY = Severity.WARN;
        final String READABLE = "[\\x20-\\x7E]";
        final String NONREADABLE = "[^\\x20-\\x7E]";
        String badChars = stringProperty.trim().replaceAll(READABLE, "");
        if (badChars.length() > 0) {
            final String MSG = String.format("Unreadable character(s) %s found in %s will be deleted",
                    Arrays.toString(badChars.toCharArray()), stringProperty);
            final String ERRORMSG = String.format("Unreadable character(s) found in %s ", propertyName);

//            if (level == Severity.ERROR) {  TODO: remove
//                summaryLog.addToReport(ERRORMSG, level);
//                throw new IllegalArgumentException(MSG);
//            }

            log.addToReport(MSG, UNREADABLECHARSEVERITY);

            return stringProperty.replaceAll(NONREADABLE, "")
                    .trim().toLowerCase();
        }
        return stringProperty;
    }

    public BigDecimal parsePrice(String priceString, String propertyName, Severity level) {

        // check string
        final Severity EMPTYSTRINGSEVERITY = Severity.WARN;
        priceString = parseString(priceString, propertyName, EMPTYSTRINGSEVERITY);
        if (priceString.isEmpty()) return null;

        // check comma's
        final Severity COMMASEVERITY = Severity.INFO;
        final String COMMA_MSG = String.format("Commas in %s will be replaced by periods", propertyName);
        final String HASCOMMA = ".*,.*";
        if (priceString.trim().matches(HASCOMMA)) {
            log.addToReport(COMMA_MSG, COMMASEVERITY);
        }
        priceString = priceString.replace(',', '.');

        // check non-numeric chars
        final String NUMERIC = "[0-9.]";
        final String NONNUMERIC = "[^0-9.]";
        String badChars = priceString.trim().replaceAll(NUMERIC, "");
        if (badChars.length() > 0) {
            final String NUM_MSG = String.format("Non-numeric character(s) %s in %s will be deleted",
                    Arrays.toString(badChars.toCharArray()), propertyName);
            final String ERRORMSG = String.format("Non-numeric character(s) %s in %s",
                    Arrays.toString(badChars.toCharArray()), propertyName);

            if (level == Severity.ERROR) {
                log.addToReport(ERRORMSG, level);
                throw new IllegalArgumentException(ERRORMSG);
            }

            log.addToReport(NUM_MSG, level);
            priceString.trim().replaceAll(NONNUMERIC, "");
        }

        return new BigDecimal(priceString);
    }

    public int parseInteger(String integerString, String propertyName, Severity level) {
        return Integer.parseInt(parseString(integerString, propertyName, level));
    }

    public boolean parseBoolean(String booleanString, String propertyName, Severity level) {
        final String trueValue = "JA";
        final String falseValue = "NEE";

        String value = parseString(booleanString, propertyName, level);
        if (!(value.equals(trueValue) || value.equals(falseValue))){
            final String MSG = String.format("Value %s in %s not recognized as boolean value",
                    booleanString, propertyName);
            final String ERRORMSG = String.format("Value %s in %s not recognized as boolean value. " +
                            "Value set to false.", booleanString, propertyName);

            if (level == Severity.ERROR) {
                log.addToReport(ERRORMSG, level);
                throw new IllegalArgumentException(MSG);
            }

            log.addToReport(MSG, level);
        }

        return value.equalsIgnoreCase(trueValue);
    }

    protected ProductCsvLine parseProduct(HasProductData raw) {
        String categoryName = parseString(raw.getCategoryName(), "category name", Severity.ERROR);
        String subCategoryName = parseString(raw.getSubCategoryName(), "subcategory name", Severity.ERROR);
        String productName = parseString(raw.getProductName(), "product name", Severity.ERROR);
        String productDescription = parseString(raw.getProductDescription(),
                "product description", Severity.WARN);
        BigDecimal price = parsePrice(raw.getPrice(), "product price", Severity.ERROR);
        boolean isSpicy = parseBoolean(raw.getIsSpicy(),
                "product spicy indicator", Severity.WARN);
        boolean isVegetarian = parseBoolean(raw.getIsVegetarian(),
                "product vegetarian indicator", Severity.WARN);

        return new ProductCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, isSpicy, isVegetarian);
    }

    public void report() {
        log.report();
    }
}
