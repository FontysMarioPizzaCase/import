package me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataValidator;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrustsDataValidator extends DataValidator<CrustRawCsvLine> {

    public CrustsDataValidator(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory);
    }

    @Override
    protected void validate(CrustRawCsvLine line) {
        if (line.getCrustName().isEmpty()) {
            extendedLogger.processWarning("Record does not have a crust name");
            throw new IllegalArgumentException();
        }
        if (line.getAddPrice().isEmpty()){
            extendedLogger.processWarning("Record does not have a crust surcharge");
            throw new IllegalArgumentException();
        }
        if (line.getSize().isEmpty()) {
            extendedLogger.processWarning("Record does not have a crust size");
            throw new IllegalArgumentException();
        }
        if (line.getDescription().isEmpty()) {
            extendedLogger.processWarning("Record does not have a crust description");
        }
        if (line.getIsAvailable().isEmpty()) {
            extendedLogger.processWarning("Record does not have an availability indicator");
        }
    }
}
