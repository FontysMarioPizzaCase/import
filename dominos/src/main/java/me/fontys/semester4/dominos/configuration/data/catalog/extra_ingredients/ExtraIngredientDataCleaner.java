package me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients;

import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.general.DataCleaner;
import me.fontys.semester4.dominos.configuration.data.catalog.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExtraIngredientDataCleaner extends DataCleaner<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {

    public ExtraIngredientDataCleaner(ExtendedLoggerFactory extendedLoggerFactory, CleanerUtil util) {
        super(extendedLoggerFactory, util);
    }

    @Override
    protected ExtraIngredientCsvLine clean(ExtraIngredientRawCsvLine raw) {
        String ingredientName = util.cleanString(raw.getIngredientName());
        BigDecimal addPrice = util.cleanPrice(raw.getAddPrice());

        return new ExtraIngredientCsvLine(ingredientName, addPrice);
    }
}
