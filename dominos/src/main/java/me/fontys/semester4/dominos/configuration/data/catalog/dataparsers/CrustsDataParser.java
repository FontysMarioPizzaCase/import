package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.ValidatorUtilFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CrustsDataParser extends DataParser<CrustRawCsvLine, CrustCsvLine> {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public CrustsDataParser(DatabaseLoggerFactory databaseLoggerFactory,
                            CleanerUtil cleanerUtil, ValidatorUtilFactory validatorUtilFactory) {
        super(databaseLoggerFactory, cleanerUtil, validatorUtilFactory);
    }

    @Override
    protected CrustCsvLine clean(CrustRawCsvLine raw) {
        String crustName = cleanerUtil.cleanString(raw.getCrustName());
        String description = cleanerUtil.cleanString(raw.getDescription());
        int size = cleanerUtil.cleanInteger(raw.getSize());
        BigDecimal addPrice = cleanerUtil.cleanPrice(raw.getAddPrice());
        boolean isAvailable = cleanerUtil.cleanBool(raw.getIsAvailable(), "JA");

        return new CrustCsvLine(crustName, description, size, addPrice, isAvailable);
    }

    @Override
    protected void validate(CrustRawCsvLine line) throws IllegalArgumentException {
        validatorUtil.validateNotEmpty(line.getCrustName(), "crust name", Severity.ERROR);
        validatorUtil.validateNotEmpty(line.getAddPrice(), "crust surcharge", Severity.ERROR);
        validatorUtil.validatePriceFormat(line.getAddPrice(), "crust surcharge", Severity.WARN);
        validatorUtil.validateNotEmpty(line.getSize(), "crust size", Severity.ERROR);
        validatorUtil.validateNotEmpty(line.getDescription(), "crust description", Severity.WARN);
        validatorUtil.validateNotEmpty(line.getIsAvailable(), "availability indicator", Severity.WARN);
    }
}