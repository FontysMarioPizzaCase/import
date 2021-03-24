package me.fontys.semester4.dominos.configuration.data.catalog.datavalidators;

import me.fontys.semester4.data.entity.LogLevel;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.HasProductData;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class DataValidator<RawT> implements HasExtendedLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DataValidator.class);
    protected final DatabaseLogger log;

    public DataValidator(ExtendedLoggerFactory extendedLoggerFactory){
        this.log = extendedLoggerFactory.extendedLogger(LOGGER);
    }

    public List<RawT> validate(List<RawT> rawLines) {
        log.info(String.format("- Validating %d lines", rawLines.size()));
        log.clearReport();

        List<RawT> accepted = new ArrayList<>();
        for (RawT line : rawLines) {
            try {
                validate(line);
                accepted.add(line);
            }
            catch(IllegalArgumentException e){
                log.addToReport("Skipped: " + line.toString(), LogLevel.ERROR);
            }
        }
        return accepted;
    }

    protected abstract void validate(RawT line) throws IllegalArgumentException;


    protected void skipOnFail(boolean success) throws IllegalArgumentException {
        if (!success) {
            throw new IllegalArgumentException();
        }
    }

    protected boolean validateNotEmpty(String stringProperty, String name) {
        if (stringProperty.isEmpty()) {
            log.addToReport(String.format("Record does not have a %s", name), LogLevel.WARN);
            return false;
        }
        return true;
    }

    protected boolean validatePriceFormat(String stringProperty, String name) {
        boolean okToContinue = true;

        if(stringProperty.trim().matches(".*,.*")){
            log.addToReport(String.format("Commas in %s will be replaced by periods", name), LogLevel.INFO);
        }

        if(!validateNotEmpty(stringProperty, name)) okToContinue = false;

        // TODO: fix + validate and clean in 1 process?
        if(stringProperty.trim().matches(".*[^0-9.].*")){
            log.addToReport(String.format("Unexpected character in %s", name), LogLevel.WARN);
//            okToContinue = false;
        }
        return okToContinue;
    }

    protected void validateProductData(HasProductData line) {
        skipOnFail(validateNotEmpty(line.getCategoryName(), "category name"));
        skipOnFail(validateNotEmpty(line.getSubCategoryName(), "subcategory name"));
        skipOnFail(validateNotEmpty(line.getProductName(), "product name"));
        skipOnFail(validatePriceFormat(line.getPrice(), "product price"));
        validateNotEmpty(line.getProductDescription(), "product description");
        validateNotEmpty(line.getIsSpicy(), "product spicy indicator");
        validateNotEmpty(line.getIsVegetarian(), "product vegetarian indicator");
    }

    public void report(){
        log.report();
    }
}
