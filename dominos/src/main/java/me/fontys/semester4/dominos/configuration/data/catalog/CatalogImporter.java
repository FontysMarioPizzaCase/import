package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.dominos.configuration.data.catalog.extract.CsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.extract.Extractor;
import me.fontys.semester4.dominos.configuration.data.catalog.load.Loader;
import me.fontys.semester4.dominos.configuration.data.catalog.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Configuration
public class CatalogImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogImporter.class);
    private final Map<String, Integer> warnings = new HashMap<>();

    private final Resource[] resources;
    private Extractor extractor;
    private Transformer transformer;
    private Loader loader;

    @Autowired
    public CatalogImporter(@Qualifier("pizzaWithIngredients") Resource[] resources,
                           Extractor extractor, Transformer transformer, Loader loader) {
        this.resources = resources;
        this.extractor = extractor;
        this.transformer = transformer;
        this.loader = loader;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        LOGGER.info("Starting import of pizza-with-ingredients records...");
        this.warnings.clear();

        List<CsvLine> records = extractor.extract(resources);
        transformer.toEntities(records);
        loader.loadIntoDb(transformer.getProducts(), transformer.getCategories(),
                transformer.getIngredients(), transformer.getPrices());

        // TODO: does @Transactional automatically close and flush session? How to check?
    }





    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        extractor.report();
        transformer.report();
        loader.report();

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
