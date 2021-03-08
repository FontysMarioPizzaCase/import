package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.dominos.configuration.data.catalog.extraingredientsurcharge.ExtraIngedientSurchargeImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.PizzaWithIngredientsImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class CatalogImporter {
    private final PizzaWithIngredientsImporter pizzaWithIngredientsImporter;
    private final ExtraIngedientSurchargeImporter extraIngedientSurchargeImporter;

    @Autowired
    public CatalogImporter(PizzaWithIngredientsImporter pizzaWithIngredientsImporter,
                           ExtraIngedientSurchargeImporter extraIngedientSurchargeImporter) {
        this.pizzaWithIngredientsImporter = pizzaWithIngredientsImporter;
        this.extraIngedientSurchargeImporter = extraIngedientSurchargeImporter;
    }

    public void doImport() throws IOException {
        this.pizzaWithIngredientsImporter.doImport();
        this.extraIngedientSurchargeImporter.doImport();
    }


    public void report() {
        this.pizzaWithIngredientsImporter.report();
        this.extraIngedientSurchargeImporter.report();
    }
}
