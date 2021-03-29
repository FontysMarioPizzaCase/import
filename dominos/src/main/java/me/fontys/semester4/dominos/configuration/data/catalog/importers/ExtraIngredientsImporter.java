package me.fontys.semester4.dominos.configuration.data.catalog.importers;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper.CategoryRepoHelper;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper.IngredientRepoHelper;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper.RepoHelperFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.parsers.ExtraIngredientParser;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.extractors.ExtractorFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.other.Relationship;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
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
    private final IngredientRepoHelper ingredientRepoHelper;
    private final CategoryRepoHelper categoryRepoHelper;

    final String INGREDIENTCATEGORYNAME;
    final String EXTRAINGREDIENTCATEGORYNAME;

    @Autowired
    public ExtraIngredientsImporter(Environment environment,
                                    DatabaseLoggerFactory databaseLoggerFactory,
                                    @Qualifier("ingredientSurcharge") Resource[] resources,
                                    ExtractorFactory extractorFactory,
                                    ExtraIngredientParser cleaner,
                                    RepoHelperFactory repoHelperFactory) {
        super(environment, databaseLoggerFactory,
                resources, extractorFactory.getExtraIngredientsDataExtractor(),
                cleaner);
        this.ingredients = new HashMap<>();
        this.categories = new HashMap<>();
        this.ingredient_category = new HashSet<>();
        this.ingredientRepoHelper = repoHelperFactory.getIngredientRepoHelper(log);
        this.categoryRepoHelper = repoHelperFactory.getCategoryRepoHelper(log);

        // init constants
        INGREDIENTCATEGORYNAME = environment.getProperty(
                "catalog.pizzaingredientsimport.default_category_for_ingredients");
        EXTRAINGREDIENTCATEGORYNAME = environment.getProperty(
                "catalog.pizzaingredientsimport.default_category_for_extraingredients");
    }

    @Override
    protected void transformAndLoad(ExtraIngredientCsvLine l) {
        Ingredient ingredient = new Ingredient(null, l.getIngredientName(), null, l.getAddPrice(),
                null, true);
        Category ingredientCat = new Category(null, null, INGREDIENTCATEGORYNAME);
        Category extraIngredientCat = new Category(null, null, EXTRAINGREDIENTCATEGORYNAME);

        // save ingredient categories
        ingredientCat = categoryRepoHelper.saveOrUpdate(ingredientCat);
        categories.put(ingredientCat.getCatid(), ingredientCat);
        extraIngredientCat = categoryRepoHelper.saveOrUpdate(extraIngredientCat);
        categories.put(extraIngredientCat.getCatid(), extraIngredientCat);

        // save ingredients and cache relationships to categories
        ingredient = ingredientRepoHelper.saveOrUpdate(ingredient);
        ingredients.put(ingredient.getIngredientid(), ingredient);
        ingredient_category.add(new Relationship<>(ingredient, categories.get(ingredientCat.getCatid())));
        ingredient_category.add(new Relationship<>(ingredient, categories.get(extraIngredientCat.getCatid())));
    }

    @Override
    protected void loadCachedRelationships() {
        for (Relationship<Ingredient, Category> relation : ingredient_category) {
            relation.getLeft().addCategory(relation.getRight());
        }
    }
}
