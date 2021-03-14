package me.fontys.semester4.dominos;

import me.fontys.semester4.dominos.configuration.data.catalog.ExtraIngredientsImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.OverigeProductenImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaIngredientsImporter;
import me.fontys.semester4.dominos.configuration.data.order.OrderImporter;
import me.fontys.semester4.dominos.configuration.data.store.StoreImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication
@EntityScan(basePackages = "me.fontys.semester4")
@EnableJpaRepositories(basePackages = "me.fontys.semester4")
public class DominosImport implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DominosImport.class);


    public static void main(String[] args) {
        SpringApplication.run(DominosImport.class, args);
    }

    private final OrderImporter orderImporter;
    private final StoreImporter storeImporter;
    private final PizzaIngredientsImporter pizzaIngredientsImporter;
    private final ExtraIngredientsImporter extraIngredientsImporter;
    private OverigeProductenImporter overigeProductenImporter;

    @Autowired
    public DominosImport(OrderImporter orderImporter, StoreImporter storeImporter,
                         PizzaIngredientsImporter pizzaIngredientsImporter,
                         ExtraIngredientsImporter extraIngredientsImporter,
                         OverigeProductenImporter overigeProductenImporter) {
        this.orderImporter = orderImporter;
        this.storeImporter = storeImporter;
        this.pizzaIngredientsImporter = pizzaIngredientsImporter;
        this.extraIngredientsImporter = extraIngredientsImporter;
        this.overigeProductenImporter = overigeProductenImporter;
    }

    @Override
    public void run(String... args) throws IOException {
        long start = System.currentTimeMillis();

        // Do the order import
//         this.orderImporter.doImport();
        this.orderImporter.report();

        // Do the store import
        this.storeImporter.doImport();

        // Do the catalog imports
        this.pizzaIngredientsImporter.doImport();
        this.pizzaIngredientsImporter.report();
        this.extraIngredientsImporter.doImport();
        this.extraIngredientsImporter.report();
        this.overigeProductenImporter.doImport();
        this.extraIngredientsImporter.report();
    }
}
