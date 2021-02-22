package me.fontys.semester4.dominos.configuration.data.catalog.prepare.validate;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExtraIngredientDataValidator extends DataValidator<ExtraIngredientRawCsvLine> {

    public ExtraIngredientDataValidator(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory);
    }

    @Override
    protected void validate(ExtraIngredientRawCsvLine line) {
        if (line.getIngredientName().isEmpty()) {
            extendedLogger.processWarning("Record does not have an ingredient name");
            throw new IllegalArgumentException();
        }
        if (line.getAddPrice().isEmpty()){
            extendedLogger.processWarning("Record does not have a ingredient surcharge");
            throw new IllegalArgumentException();
        }
    }
}
