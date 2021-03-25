package me.fontys.semester4.dominos.configuration.data.catalog.datacleaners;

import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExtraIngredientDataCleaner extends DataCleaner<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {

    public ExtraIngredientDataCleaner(DatabaseLoggerFactory databaseLoggerFactory, CleanerUtil util) {
        super(databaseLoggerFactory, util);
    }

    @Override
    protected ExtraIngredientCsvLine clean(ExtraIngredientRawCsvLine raw) {
        String ingredientName = util.cleanString(raw.getIngredientName());
        BigDecimal addPrice = util.cleanPrice(raw.getAddPrice());

        return new ExtraIngredientCsvLine(ingredientName, addPrice);
    }
}
