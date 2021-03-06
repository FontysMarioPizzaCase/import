package me.fontys.semester4.dominos.configuration.data.catalog;

import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

@Configuration
public class CatalogImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogImporter.class);

    @Qualifier("catalogResources")
    private final Resource[] catalogResources;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPriceRepository productPriceRepository;
    private final CatalogConverter catalogConverter;
    private final Map<String, Integer> warnings = new HashMap<>();

    @Autowired
    public CatalogImporter(Resource[] catalogResources, ProductRepository productRepository,
                           IngredientRepository ingredientRepository,
                           CategoryRepository categoryRepository,
                           ProductPriceRepository productPriceRepository,
                           CatalogConverter catalogConverter) {
        this.catalogResources = catalogResources;
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
        this.productPriceRepository = productPriceRepository;
        this.catalogConverter = catalogConverter;
    }

    public void doImport() throws IOException {
        LOGGER.info("Starting import of catalog records...");

        this.warnings.clear();
        List<PizzaAndIngredientRecord> records = new ArrayList<>();

        for (Resource resource : this.catalogResources) {
            LOGGER.info("Reading resource " + resource.getFilename());
            records.addAll(this.processPizzaAndIngredientCsv(resource, ';'));
        }

        // convert records
        CatalogConverter c = this.catalogConverter;
        c.convert(records);

        // import entities
        LOGGER.info(String.format("Inserting entities..."));
        for (Product p : c.getProducts()) {
            boolean exists = this.productRepository.existsByName(p.getName());
            if (exists) {
                Product pOld = this.productRepository.findByName(p.getName()).get();
                if (pOld.equals(p)) {
                    processWarning("Duplicate product not inserted");
                }
                if (!pOld.equals(p)) {
                    processWarning("Update candidate found for product. Add or update?");
                }
            } else {
                this.productRepository.save(p);
            }
        }

//        this.categoryRepository.saveAll(c.getCategories());
//        this.ingredientRepository.saveAll(c.getIngredients());
//        this.productPriceRepository.saveAll(c.getPrices());
    }

    private List<PizzaAndIngredientRecord> processPizzaAndIngredientCsv(
            Resource resource, char separator) throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream()));

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

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        this.catalogConverter.report();

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
