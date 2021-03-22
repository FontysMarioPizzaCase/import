package me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.dominos.configuration.data.catalog.general.*;
import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.general.helper_models.Relationship;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.util.*;

@Configuration
public class ExtraIngredientsImporter extends CsvImporter<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {
    private final Map<Long, Ingredient> ingredients;
    private final Map<Long, Category> categories;
    private final Set<Relationship<Ingredient, Category>> ingredient_category;

    @Autowired
    public ExtraIngredientsImporter(Environment environment,
                                    ExtendedLoggerFactory extendedLoggerFactory,
                                    @Qualifier("ingredientSurcharge") Resource[] resources,
                                    ExtraIngredientsDataExtractor dataExtractor,
                                    ExtraIngredientDataValidator validator, ExtraIngredientDataCleaner cleaner,
                                    DatabaseLoader loader) {
        super(environment, extendedLoggerFactory, resources, dataExtractor, validator, cleaner, loader);
        this.ingredients = new HashMap<>();
        this.categories = new HashMap<>();
        this.ingredient_category = new HashSet<>();
    }

    @Override
    protected void doImport(List<ExtraIngredientCsvLine> csvLines) {
        super.doImport(csvLines); // calls transformAndLoad below
        loadCachedRelationships();
    }

    @Override
    protected void transformAndLoad(ExtraIngredientCsvLine l) {
        final String INGREDIENTCATEGORYNAME = environment.getProperty(
                "catalog.pizzaingredientsimport.default_category_for_ingredients");

        Ingredient ingredient = new Ingredient(null, l.getIngredientName(), null, l.getAddPrice(),
                null, true);
        Category category = new Category(null, null, INGREDIENTCATEGORYNAME);

        // save ingredient categories
        category = loader.toDb(category);
        categories.put(category.getCatid(), category);

        // save ingredients and cache relationships to categories
        ingredient = loader.toDb(ingredient);
        ingredients.put(ingredient.getIngredientid(), ingredient);
        ingredient_category.add(new Relationship<>(ingredient, categories.get(category.getCatid())));
    }

    protected void loadCachedRelationships() {
        for (Relationship<Ingredient, Category> relation : ingredient_category) {
            relation.getLeft().addCategory(relation.getRight());
        }
    }
}
