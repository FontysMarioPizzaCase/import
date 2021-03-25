package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.dominos.configuration.data.catalog.importers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CatalogImporter {

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

    public void report() {
        for (var importer : importers) {
            importer.report();
        }
    }
}
