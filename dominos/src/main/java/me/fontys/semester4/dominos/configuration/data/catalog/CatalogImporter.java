package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.dominos.configuration.data.catalog.extraingredientsurcharge.ExtraIngedientSurchargeImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.Importer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class CatalogImporter {
    private final Importer importer;
    private final ExtraIngedientSurchargeImporter extraIngedientSurchargeImporter;

    @Autowired
    public CatalogImporter(Importer importer,
                           ExtraIngedientSurchargeImporter extraIngedientSurchargeImporter) {
        this.importer = importer;
        this.extraIngedientSurchargeImporter = extraIngedientSurchargeImporter;
    }

    public void doImport() throws IOException {
        this.importer.doImport();
        this.extraIngedientSurchargeImporter.doImport();
    }


    public void report() {
        this.importer.report();
        this.extraIngedientSurchargeImporter.report();
    }
}
