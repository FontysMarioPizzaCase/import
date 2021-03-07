package me.fontys.semester4.dominos.configuration.data.catalog;

import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.Converters.CategoryImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.Converters.IngredientImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.Converters.ProductImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;

@Configuration
public class CatalogImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogImporter.class);

    @Qualifier("catalogResources")
    private final Resource[] catalogResources;
    private final List<ProductPrice> prices;
    private final Map<String, Integer> warnings = new HashMap<>();
    private final ProductImporter productImporter;
    private final IngredientImporter ingredientImporter;
    private final CategoryImporter categoryImporter;

    @Autowired
    public CatalogImporter(Resource[] catalogResources, ProductImporter productImporter,
                           IngredientImporter ingredientImporter, CategoryImporter categoryImporter) {
        this.catalogResources = catalogResources;

        this.productImporter = productImporter;
        this.ingredientImporter = ingredientImporter;
        this.categoryImporter = categoryImporter;
        this.prices = new ArrayList<>();
    }

    public void doImport() throws IOException {
        LOGGER.info("Starting import of catalog records...");
        this.warnings.clear();

        List<PizzaAndIngredientRecord> records = processResources();
        convertToEntities(records);
        persistEntities();
    }

    private List<PizzaAndIngredientRecord> processResources() throws IOException {
        List<PizzaAndIngredientRecord> records = new ArrayList<>();

        for (Resource resource : this.catalogResources) {
            LOGGER.info("Reading resource " + resource.getFilename());
            records.addAll(this.processCsv(resource, ';'));
        }
        return records;
    }

    private List<PizzaAndIngredientRecord> processCsv(Resource resource,
                                                      char separator) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resource.getInputStream()));

        List<PizzaAndIngredientRecord> records = new CsvToBeanBuilder(reader)
                .withSeparator(separator)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .withIgnoreLeadingWhiteSpace(true)
                .withType(PizzaAndIngredientRecord.class)
                .build()
                .parse();

        return records;
    }

    private void convertToEntities(List<PizzaAndIngredientRecord> records) {
        LOGGER.info(String.format("Converting %s records...", records.size()));

        for (var record : records) {
            Product product = productImporter.process(record);
            Ingredient ingredient = ingredientImporter.process(record);
            List<Category> categories = categoryImporter.process(record);

            // TODO: update relationships
            product.getIngredients().add(ingredient);
            product.getCategories().addAll(categories);

            // TODO: pizzasaus, aantalkeer_ingredient, beschikbaar
            // TODO: validate();
            // TODO: priceImporter
            ProductPrice newPrice = extractPrice(record, product.getProductid());
        }

        LOGGER.info(String.format("Conversion result: "));
        LOGGER.info(String.format("- %s prices", this.prices.size()));
    }

    private ProductPrice extractPrice(PizzaAndIngredientRecord record, Long productId) {
        if (record.getPrice().isEmpty()) {
            processWarning("Record does not have a product price");
        }

        return new ProductPrice(
                null,
                productId,
                record.getPrice(),
                new Date()
        );
    }

    private void persistEntities() {
        // TODO: productImporter.saveAll();


//        LOGGER.info(String.format("Inserting entities..."));
//        for (Product p : c.getProducts()) {
//            boolean exists = this.productRepository.existsByName(p.getName());
//            if (exists) {
//                Product pOld = this.productRepository.findByName(p.getName()).get();
//                if (pOld.equals(p)) {
//                    processWarning("Duplicate product not inserted");
//                }
//                if (!pOld.equals(p)) {
//                    processWarning("Update candidate found for product. Add or update?");
//                }
//            } else {
//                this.productRepository.save(p);
//            }
//        }

//        this.categoryRepository.saveAll(c.getCategories());
//        this.ingredientRepository.saveAll(c.getIngredients());
//        this.productPriceRepository.saveAll(c.getPrices());
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
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
