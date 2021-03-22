package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.dominos.configuration.data.catalog.importers.*;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasExtendedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CatalogImporter implements HasExtendedLogger {

    private final List<Importer> importers;

    @Autowired
    public CatalogImporter(PizzaIngredientsImporter pizzaIngredientsImporter,
                           OtherProductsImporter otherProductsImporter,
                           ExtraIngredientsImporter extraIngredientsImporter,
                           CrustsImporter crustsImporter) {
        importers = new ArrayList<>();
        importers.add(pizzaIngredientsImporter);
        importers.add(otherProductsImporter);
        importers.add(extraIngredientsImporter);
        importers.add(crustsImporter);
    }

    public void doImport() throws IOException {
        for (var importer : importers) {
            importer.doImport();
        }
    }

    @Override
    public void report() {
        for (var importer : importers) {
            importer.report();
        }
    }
}
