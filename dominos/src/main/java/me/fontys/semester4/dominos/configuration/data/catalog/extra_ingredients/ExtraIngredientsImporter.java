package me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients;

import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.dominos.configuration.data.catalog.general.*;
import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExtraIngredientsImporter extends CsvImporter<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {
    private final Map<Long, Ingredient> ingredients;

    @Autowired
    public ExtraIngredientsImporter(ExtendedLoggerFactory extendedLoggerFactory,
                                    @Qualifier("ingredientSurcharge") Resource[] resources,
                                    ExtraIngredientsDataExtractor dataExtractor,
                                    ExtraIngredientDataValidator validator, ExtraIngredientDataCleaner cleaner,
                                    DatabaseLoader loader) {
        super(extendedLoggerFactory, resources, dataExtractor, validator, cleaner, loader);
        this.ingredients = new HashMap<>();
    }

    @Override
    protected void transformAndLoad(ExtraIngredientCsvLine l) {
        Ingredient ingredient = new Ingredient(null, l.getIngredientName(), l.getAddPrice());

        if (!ingredients.containsKey(ingredient.getIngredientid())) {
            ingredient = loader.toDb(ingredient);
            ingredients.put(ingredient.getIngredientid(), ingredient);
        }
    }
}
