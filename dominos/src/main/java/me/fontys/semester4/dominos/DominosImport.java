package me.fontys.semester4.dominos;

import me.fontys.semester4.dominos.configuration.data.catalog.CatalogImporter;
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
    private final CatalogImporter catalogImporter;

    @Autowired
    public DominosImport(OrderImporter orderImporter,
                         StoreImporter storeImporter,
                         CatalogImporter catalogImporter
    ) {
        this.orderImporter = orderImporter;
        this.storeImporter = storeImporter;
        this.catalogImporter = catalogImporter;
    }

    @Override
    public void run(String... args) throws IOException {
        long start = System.currentTimeMillis();

        // Do the order import
        // this.orderImporter.doImport(); TODO: PUT BACK
        this.orderImporter.report();

        // Do the store import
        this.storeImporter.doImport();

        // Do the catalog imports
        this.catalogImporter.doImport();
        this.catalogImporter.report();
    }
}
