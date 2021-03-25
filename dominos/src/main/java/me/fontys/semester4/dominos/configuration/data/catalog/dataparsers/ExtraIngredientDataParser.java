package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.ValidatorUtilFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExtraIngredientDataParser extends DataParser<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public ExtraIngredientDataParser(DatabaseLoggerFactory databaseLoggerFactory,
                                     CleanerUtil cleanerUtil, ValidatorUtilFactory validatorUtilFactory) {
        super(databaseLoggerFactory, cleanerUtil, validatorUtilFactory);
    }

    @Override
    protected ExtraIngredientCsvLine clean(ExtraIngredientRawCsvLine raw) {
        String ingredientName = cleanerUtil.cleanString(raw.getIngredientName());
        BigDecimal addPrice = cleanerUtil.cleanPrice(raw.getAddPrice());

        return new ExtraIngredientCsvLine(ingredientName, addPrice);
    }

    @Override
    protected void validate(ExtraIngredientRawCsvLine line) {
        validatorUtil.validateNotEmpty(line.getIngredientName(), "ingredient name", Severity.ERROR);
        validatorUtil.validateNotEmpty(line.getAddPrice(), "ingredient surcharge", Severity.ERROR);
        validatorUtil.validatePriceFormat(line.getAddPrice(), "ingredient surcharge", Severity.WARN);
    }
}
