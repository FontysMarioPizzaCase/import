package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.dominos.configuration.data.catalog.load.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.ExtraIngredientDataPrepper;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExtraIngredientsImporter extends CatalogImporter<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {
    private final Map<Long, Ingredient> ingredients;

    @Autowired
    public ExtraIngredientsImporter(ExtendedLoggerFactory extendedLoggerFactory,
                                    @Qualifier("ingredientSurcharge") Resource[] resources,
                                    ExtraIngredientDataPrepper extraIngredientDataExtractor,
                                    DatabaseLoader loader) {
        super(extendedLoggerFactory, resources, extraIngredientDataExtractor, loader);
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
