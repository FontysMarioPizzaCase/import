package me.fontys.semester4.dominos.configuration.data.catalog.datavalidators;

import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.*;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrustsDataValidator extends DataValidator<CrustRawCsvLine> {

    public CrustsDataValidator(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory);
    }

    @Override
    protected void validate(CrustRawCsvLine line) throws IllegalArgumentException {
        skipOnFail(validateNotEmpty(line.getCrustName(), "crust name"));
        skipOnFail(validatePriceFormat(line.getAddPrice(), "crust surcharge"));
        skipOnFail(validateNotEmpty(line.getSize(), "crust size"));
        validateNotEmpty(line.getDescription(), "crust description");
        validateNotEmpty(line.getIsAvailable(), "availability indicator");
    }
}
