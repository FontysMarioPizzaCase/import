package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.extract.CsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.extract.DataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.load.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Configuration
public class CatalogImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogImporter.class);
    private final Map<String, Integer> warnings = new HashMap<>();

    private final Resource[] resources;
    private DataExtractor dataExtractor;
    private Loader loader;

    private Set<Ingredient> ingredients;
    private Set<Category> categories;
    private Set<Product> products;
    private Set<ProductPrice> prices;

    Map<Category, Category> categoryParentCache = new HashMap<>();
    Map<ProductPrice, Product> priceProductCache = new HashMap<>();
    Map<Product, Ingredient> productIngredientCache = new HashMap<>();
    Map<Product, Category> productCategoryCache = new HashMap<>();

    @Autowired
    public CatalogImporter(@Qualifier("pizzaWithIngredients") Resource[] resources,
                           DataExtractor dataExtractor, Loader loader) {
        this.resources = resources;
        this.dataExtractor = dataExtractor;
        this.loader = loader;

        ingredients = new HashSet<>();
        categories = new HashSet<>();
        products = new HashSet<>();
        prices = new HashSet<>();
        categoryParentCache = new HashMap<>();
        priceProductCache = new HashMap<>();
        productIngredientCache = new HashMap<>();
        productCategoryCache = new HashMap<>();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        LOGGER.info("Starting import of pizza-with-ingredients csvLines...");
        this.warnings.clear();

        List<CsvLine> csvLines = dataExtractor.extract(resources);
        transformAndSave(csvLines);
    }


    public void transformAndSave(List<CsvLine> csvLines) {
        LOGGER.info(String.format("Converting %s csvLines...", csvLines.size()));
        this.warnings.clear();

        // TODO: pizzasaus (+ contraint), aantalkeer_ingredient, beschikbaar

        for (var line : csvLines) {
            try {
                transformAndSave(line);
            } catch (Exception e) {
                processWarning(String.format("Invalid data in line: %s || ERROR: %s",
                        line.toString(), e.toString()));
                throw e;
            }
        }
        for (Map.Entry<Category, Category> entry : categoryParentCache.entrySet()) {
            entry.getKey().setParent(entry.getValue());
        }
        for (Map.Entry<ProductPrice, Product> entry : priceProductCache.entrySet()) {
            entry.getKey().setProduct(entry.getValue());
        }
        for (Map.Entry<Product, Ingredient> entry : productIngredientCache.entrySet()) {
            entry.getKey().getIngredients().add(entry.getValue());
        }
        for (Map.Entry<Product, Category> entry : productCategoryCache.entrySet()) {
            entry.getKey().getCategories().add(entry.getValue());
        }
    }

    private void transformAndSave(CsvLine l) {
        final double TAXRATE = 0.06; // TODO: user input?
        final Date FROMDATE = new Date();

        Product product = new Product(null, l.getProductName(), l.getProductDescription(),
                l.isSpicy(), l.isVegetarian(), l.getDeliveryFee(), TAXRATE, null);
        Category parent = new Category(null, null, l.getCategoryName());
        Category child = new Category(null, null, l.getSubCategoryName());
        Ingredient ingredient = new Ingredient(null, l.getIngredientName(), null);
        ProductPrice price = new ProductPrice(null, product, l.getPrice(), FROMDATE);

        if (!products.contains(product)) {
            product = loader.toDb(product);
            products.add(product);
        }

        if (!categories.contains(child)) {
            child = loader.toDb(child);
            categories.add(child);
        }

        if (!categories.contains(parent)) {
            parent = loader.toDb(parent);
            categories.add(parent);
        }

        if (!ingredients.contains(ingredient)) {
            ingredient = loader.toDb(ingredient);
            ingredients.add(ingredient);
        }

        if (!prices.contains(price)) {
            price = loader.toDb(price, product);
            prices.add(price);
        }

        categoryParentCache.put(child, parent);
        priceProductCache.put(price, product);
        productIngredientCache.put(product, ingredient);
        productCategoryCache.put(product, parent);
        productCategoryCache.put(product, child);
    }


    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        dataExtractor.report();
        loader.report();

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Catalog import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
