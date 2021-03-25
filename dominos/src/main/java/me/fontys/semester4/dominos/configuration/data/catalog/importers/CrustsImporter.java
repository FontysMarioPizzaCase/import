package me.fontys.semester4.dominos.configuration.data.catalog.importers;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.dominos.configuration.data.catalog.dataloader.DatabaseLoaderFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.CrustsDataParser;
import me.fontys.semester4.dominos.configuration.data.catalog.extractors.DataExtractorFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.dataloader.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.models.helper_models.Relationship;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    @Autowired
    public CrustsImporter(Environment environment,
                          DatabaseLoggerFactory databaseLoggerFactory,
                          @Qualifier("pizzacrusts") Resource[] resources,
                          DataExtractorFactory dataExtractorFactory,
                          CrustsDataParser parser,
                          DatabaseLoaderFactory databaseLoaderFactory) {
        super(environment, databaseLoggerFactory,
                resources, dataExtractorFactory.getCrustsDataExtractor(),
                parser, databaseLoaderFactory);
        this.crusts = new HashMap<>();
        this.categories = new HashMap<>();
        this.crust_category = new HashSet<>();
    }

    @Override
    protected void transformAndLoad(CrustCsvLine l) {
        final String CATEGORYNAME = environment.getProperty(
                "catalog.pizzaingredientsimport.default_category_for_crust");

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

    @Override
    protected void loadCachedRelationships() {
        for (Relationship<Ingredient, Category> relation : crust_category) {
            relation.getLeft().addCategory(relation.getRight());
        }
    }
}
