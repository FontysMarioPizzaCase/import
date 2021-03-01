package me.fontys.semester4.dominos;

import me.fontys.semester4.dominos.configuration.data.order.OrderImporter;
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

    @Autowired
    public DominosImport(OrderImporter orderImporter) {
        this.orderImporter = orderImporter;
    }

    @Override
    public void run(String... args) throws IOException {
        long start = System.currentTimeMillis();

        // Do the import
        this.orderImporter.doImport();

        long timeElapsed = System.currentTimeMillis() - start;
        LOGGER.info(String.format("Finished import, took %s seconds.", timeElapsed / 1000));
    }
}
