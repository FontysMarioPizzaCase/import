package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.*;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CatalogImporter {

    private final List<Importer> importers;
    private final DatabaseLogger log;

    @Autowired
    public CatalogImporter(DatabaseLoggerFactory databaseLoggerFactory,
                           PizzaIngredientsImporter pizzaIngredientsImporter,
                           OtherProductsImporter otherProductsImporter,
                           ExtraIngredientsImporter extraIngredientsImporter,
                           CrustsImporter crustsImporter) {
        this.log = databaseLoggerFactory.newDatabaseLogger(this.getClass().getName());
        importers = new ArrayList<>();
        importers.add(otherProductsImporter);
        importers.add(extraIngredientsImporter);
        importers.add(crustsImporter);
        importers.add(pizzaIngredientsImporter);
    }

    public void doImport() {
        for (var importer : importers) {
            try {
                importer.doImport();
            } catch (Exception e) {
                String errormsg = String.format("Exception caught during %s. Rollback initiated. Exception: %s",
                        importer.getClass().getSimpleName(), e);
                log.error(errormsg);
                log.addToReport(errormsg, Severity.ERROR);
            }
        }
    }

    public void report() {
        for (var importer : importers) {
            importer.report();
        }
    }
}
