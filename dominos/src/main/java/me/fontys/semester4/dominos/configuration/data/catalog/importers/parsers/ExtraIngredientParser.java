package me.fontys.semester4.dominos.configuration.data.catalog.importers.parsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExtraIngredientParser extends Parser<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {

    public ExtraIngredientParser(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected ExtraIngredientCsvLine parse(ExtraIngredientRawCsvLine raw) {
        String ingredientName = parseString(raw.getIngredientName(), "ingredient name", Severity.ERROR);
        BigDecimal addPrice = parsePrice(raw.getAddPrice(), "ingredient surcharge", Severity.ERROR);

        return new ExtraIngredientCsvLine(ingredientName, addPrice);
    }
}
