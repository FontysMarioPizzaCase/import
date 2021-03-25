package me.fontys.semester4.dominos.configuration.data.catalog.datavalidators;

import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExtraIngredientDataValidator extends DataValidator<ExtraIngredientRawCsvLine> {

    public ExtraIngredientDataValidator(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected void validate(ExtraIngredientRawCsvLine line) {
        skipOnFail(validateNotEmpty(line.getIngredientName(), "ingredient name"));
        skipOnFail(validatePriceFormat(line.getAddPrice(), "ingredient surcharge"));
    }
}
