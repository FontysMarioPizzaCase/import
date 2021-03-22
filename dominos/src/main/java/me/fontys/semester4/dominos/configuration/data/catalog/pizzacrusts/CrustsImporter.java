package me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.dominos.configuration.data.catalog.general.CsvImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.general.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.general.helper_models.Relationship;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.*;

@Configuration
public class CrustsImporter extends CsvImporter<CrustRawCsvLine, CrustCsvLine> {
    private final Map<Long, Ingredient> crusts;
    private final Map<Long, Category> categories;
    private final Set<Relationship<Ingredient, Category>> crust_category;


    @Autowired
    public CrustsImporter(ExtendedLoggerFactory extendedLoggerFactory,
                          @Qualifier("pizzacrusts") Resource[] resources,
                          CrustsDataExtractor dataExtractor,
                          CrustsDataValidator validator, CrustsDataCleaner cleaner,
                          DatabaseLoader loader) {
        super(extendedLoggerFactory, resources, dataExtractor, validator, cleaner, loader);
        this.crusts = new HashMap<>();
        this.categories = new HashMap<>();
        this.crust_category = new HashSet<>();
    }

    @Override
    protected void doImport(List<CrustCsvLine> csvLines) {
        super.doImport(csvLines); // calls transformAndLoad below
        loadCachedRelationships();
    }

    @Override
    protected void transformAndLoad(CrustCsvLine l) {
        final String CATEGORYNAME = "pizza crust"; // TODO: move to settings

        Ingredient crust = new Ingredient(null, l.getCrustName(), l.getDescription(), l.getAddPrice(),
                l.getSize(), l.isAvailable());
        Category category = new Category(null, null, CATEGORYNAME);

        // save ingredient categories
        category = loader.toDb(category);
        categories.put(category.getCatid(), category);

        // save ingredients and cache relationship to categories
        crust = loader.toDb(crust);
        crusts.put(crust.getIngredientid(), crust);
        crust_category.add(new Relationship<>(crust, categories.get(category.getCatid())));
    }

    protected void loadCachedRelationships() {
        for (Relationship<Ingredient, Category> relation : crust_category) {
            relation.getLeft().addCategory(relation.getRight());
        }
    }
}
