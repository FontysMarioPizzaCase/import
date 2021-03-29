package me.fontys.semester4.dominos;

import me.fontys.semester4.dominos.configuration.data.catalog.*;
import me.fontys.semester4.dominos.configuration.data.order.OrderImporter;
import me.fontys.semester4.dominos.configuration.data.store.StoreImporter;
import me.fontys.semester4.interfaces.Importer;
import me.fontys.semester4.postalcode.PCImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "me.fontys.semester4")
@ComponentScan(basePackages = "me.fontys.semester4")
@EnableJpaRepositories(basePackages = "me.fontys.semester4")
public class DominosImport implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DominosImport.class);

    public static void main(String[] args) {
        SpringApplication.run(DominosImport.class, args);
    }

    private final OrderImporter orderImporter;
    private final StoreImporter storeImporter;
    private final CatalogImporter catalogImporter;
    private final Importer pcImporter;


    @Autowired
    public DominosImport(OrderImporter orderImporter, StoreImporter storeImporter,
                         CatalogImporter catalogImporter, PCImporter pcImporter) {
        this.orderImporter = orderImporter;
        this.storeImporter = storeImporter;
        this.catalogImporter = catalogImporter;
        this.pcImporter = pcImporter;
    }

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();

//        // Do the store import
//        this.storeImporter.doImport();
//        this.storeImporter.report();
//
//        // Do the postalcode import
//        pcImporter.doImport();
//        pcImporter.report();

        // Do the catalog imports
        this.catalogImporter.doImport();
        this.catalogImporter.report();

//        // Do the order import
//        this.orderImporter.doImport();
//        this.orderImporter.report();
//        this.orderImporter.test();

        long timeElapsed = System.currentTimeMillis() - start;
        LOGGER.info(String.format("Finished import, took %s seconds.", timeElapsed / 1000));
    }
}
