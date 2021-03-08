package me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.dominos.configuration.data.catalog.entitymanagers.CategoryManager;
import me.fontys.semester4.dominos.configuration.data.catalog.entitymanagers.IngredientManager;
import me.fontys.semester4.dominos.configuration.data.catalog.entitymanagers.ProductManager;
import me.fontys.semester4.dominos.configuration.data.catalog.entitymanagers.ProductPriceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Configuration
public class PizzaWithIngredientsImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PizzaWithIngredientsImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final Resource[] resources;
    private final ProductManager productManager;
    private final IngredientManager ingredientManager;
    private final CategoryManager categoryManager;
    private final ProductPriceManager productPriceManager;

    @Autowired
    public PizzaWithIngredientsImporter(@Qualifier("pizzaWithIngredients") Resource[] resources,
                                        ProductManager productManager,
                                        IngredientManager ingredientManager,
                                        CategoryManager categoryManager,
                                        ProductPriceManager productPriceManager) {
        this.resources = resources;

        this.productManager = productManager;
        this.ingredientManager = ingredientManager;
        this.categoryManager = categoryManager;
        this.productPriceManager = productPriceManager;
    }

    @Transactional
    public void doImport() throws IOException {
        LOGGER.info("Starting import of pizza-with-ingredients records...");
        this.warnings.clear();

        List<PizzaAndIngredientRecord> records = processResources();
        importEntities(records);
    }

    private List<PizzaAndIngredientRecord> processResources() throws IOException {
        List<PizzaAndIngredientRecord> records = new ArrayList<>();

        for (Resource resource : this.resources) {
            LOGGER.info("Reading resource " + resource.getFilename());
            records.addAll(this.processCsv(resource));
        }
        return records;
    }

    private List<PizzaAndIngredientRecord> processCsv(Resource resource) throws IOException {
        final char separator = ';';

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resource.getInputStream()));

        CsvToBean<PizzaAndIngredientRecord> beans = new CsvToBeanBuilder<PizzaAndIngredientRecord>(reader)
                .withSeparator(separator)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .withIgnoreLeadingWhiteSpace(true)
                .withType(PizzaAndIngredientRecord.class)
                .withThrowExceptions(false)
                .build();
        List<PizzaAndIngredientRecord> records = beans.parse();

        beans.getCapturedExceptions().forEach((e) -> processWarning("Inconsistent data: " + e));

        return records;
    }

    private void importEntities(List<PizzaAndIngredientRecord> records) {
        LOGGER.info(String.format("Converting %s records...", records.size()));

        for (var record : records) {
            try {
                Ingredient ingredient = ingredientManager.extractAndImport(record);
                List<Category> bothCategories = categoryManager.extractAndImport(record);
                Product product = productManager.extractAndImport(record, ingredient, bothCategories);
                productPriceManager.extractAndImport(record, product);
            } catch (Exception e) {
                processWarning(String.format("Invalid data in record: %s || ERROR: %s",
                        record.toString(), e.toString()));
            }

            // TODO: pizzasaus (+ contraint), aantalkeer_ingredient, beschikbaar
        }
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        ingredientManager.report();

        categoryManager.report();
        productManager.report();
        productPriceManager.report();

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Catalog import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
