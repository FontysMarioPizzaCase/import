package me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients;

import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.general.DataPrepper;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtraIngredientDataPrepper extends DataPrepper<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {

    @Autowired
    public ExtraIngredientDataPrepper(ExtendedLoggerFactory extendedLoggerFactory,
                                      ExtraIngredientsDataExtractor dataExtractor,
                                      ExtraIngredientDataValidator validator,
                                      ExtraIngredientDataCleaner cleaner) {
        super(extendedLoggerFactory, dataExtractor, validator, cleaner);
    }
}
