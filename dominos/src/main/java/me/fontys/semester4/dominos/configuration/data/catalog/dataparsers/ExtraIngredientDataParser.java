package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExtraIngredientDataParser extends DataParser<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {

    public ExtraIngredientDataParser(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected ExtraIngredientCsvLine parse(ExtraIngredientRawCsvLine raw) {
        String ingredientName = parseString(raw.getIngredientName(), "ingredient name", Severity.ERROR);
        BigDecimal addPrice = parsePrice(raw.getAddPrice(), "ingredient surcharge", Severity.ERROR);

        return new ExtraIngredientCsvLine(ingredientName, addPrice);
    }
}
