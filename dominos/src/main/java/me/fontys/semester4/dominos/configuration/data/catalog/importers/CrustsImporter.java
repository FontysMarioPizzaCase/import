package me.fontys.semester4.dominos.configuration.data.catalog.importers;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper.CategoryRepoHelper;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper.IngredientRepoHelper;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper.RepoHelperFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.parsers.CrustsParser;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.extractors.ExtractorFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.other.Relationship;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.util.*;

@Configuration
public class CrustsImporter extends CsvImporter<CrustRawCsvLine, CrustCsvLine> {
    private final Map<Long, Ingredient> crusts;
    private final Map<Long, Category> categories;
    private final Set<Relationship<Ingredient, Category>> crust_category;
    private final IngredientRepoHelper ingredientRepoHelper;
    private final CategoryRepoHelper categoryRepoHelper;

    final String CATEGORYNAME;

    @Autowired
    public CrustsImporter(Environment environment,
                          DatabaseLoggerFactory databaseLoggerFactory,
                          @Qualifier("pizzacrusts") Resource[] resources,
                          ExtractorFactory extractorFactory,
                          CrustsParser parser,
                          RepoHelperFactory repoHelperFactory) {
        super(environment, databaseLoggerFactory,
                resources, extractorFactory.getCrustsDataExtractor(),
                parser);
        this.crusts = new HashMap<>();
        this.categories = new HashMap<>();
        this.crust_category = new HashSet<>();
        this.ingredientRepoHelper = repoHelperFactory.getIngredientRepoHelper(log);
        this.categoryRepoHelper = repoHelperFactory.getCategoryRepoHelper(log);

        // init constants
        CATEGORYNAME = environment.getProperty(
                "catalog.pizzaingredientsimport.default_category_for_crust");
    }

    @Override
    protected void transformAndLoad(CrustCsvLine l) {
        Ingredient crust = new Ingredient(null, l.getCrustName(), l.getDescription(), l.getAddPrice(),
                l.getSize(), l.isAvailable());
        Category category = new Category(null, null, CATEGORYNAME);

        // save ingredient categories
        category = categoryRepoHelper.saveOrUpdate(category);
        categories.put(category.getCatid(), category);

        // save ingredients and cache relationship to categories
        crust = ingredientRepoHelper.saveOrUpdate(crust);
        crusts.put(crust.getIngredientid(), crust);
        crust_category.add(new Relationship<>(crust, categories.get(category.getCatid())));
    }

    @Override
    protected void loadCachedRelationships() {
        for (Relationship<Ingredient, Category> relation : crust_category) {
            relation.getLeft().addCategory(relation.getRight());
        }
    }
}
