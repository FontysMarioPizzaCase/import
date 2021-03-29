package me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper;

import me.fontys.semester4.data.entity.*;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;

import java.util.Optional;

public class ProductRepoHelper {
    protected final DatabaseLogger<LogEntry> log;
    private final ProductRepository productRepository;

    public ProductRepoHelper(DatabaseLogger<LogEntry> databaseLogger, ProductRepository productRepository) {
        this.log = databaseLogger;
        this.productRepository = productRepository;
    }

    public Product saveOrUpdate(Product newData) {
        Optional<Product> temp = this.productRepository.findByNameIgnoreCase(newData.getName());

        Product product;
        if (temp.isPresent()) {
            product = temp.get();
            updateProperties(product, newData);
        } else {
            product = newData;
            saveEntity(product);
        }

        return product;
    }

    private void saveEntity(Product product) {
        this.productRepository.save(product);
        log.addToReport("Created product " + product, Severity.INFO);
    }

    private void updateProperties(Product product, Product newData) {
        boolean isUpdated = false;

        StringBuilder logMessage = new StringBuilder();
        logMessage.append(String.format("Updating product %s: ", product.getName()));

        if (newData.getDescription() != null && !newData.getDescription().isEmpty()
                && !newData.getDescription().equals(product.getDescription())) {
            logMessage.append(String.format("[%s]=>[%s] ", product.getDescription(), newData.getDescription()));
            product.setDescription(newData.getDescription());
            isUpdated = true;
        }
        if (newData.getImagepath() != null && !newData.getImagepath().isEmpty()
                && !newData.getImagepath().equals(product.getImagepath())) {
            logMessage.append(String.format("[%s]=>[%s] ", product.getImagepath(), newData.getImagepath()));
            product.setImagepath(newData.getImagepath());
            isUpdated = true;
        }
        if (newData.getSpicy() != null && !newData.getSpicy().equals(product.getSpicy())) {
            logMessage.append(String.format("[%s]=>[%s] ", product.getSpicy(), newData.getSpicy()));
            product.setSpicy(newData.getSpicy());
            isUpdated = true;
        }
        if (newData.getVegetarian() != null && !newData.getVegetarian().equals(product.getVegetarian())) {
            logMessage.append(String.format("[%s]=>[%s] ", product.getVegetarian(), newData.getVegetarian()));
            product.setVegetarian(newData.getVegetarian());
            isUpdated = true;
        }
        if (newData.getTaxrate() != null && !newData.getTaxrate().equals(product.getTaxrate())) {
            logMessage.append(String.format("[%s]=>[%s] ", product.getTaxrate(), newData.getTaxrate()));
            product.setTaxrate(newData.getTaxrate());
            isUpdated = true;
        }

        if(!isUpdated) {
            return;
        }

        log.addToReport(logMessage.toString(), Severity.INFO);
    }
}
