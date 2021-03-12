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

    private Map<Long, Ingredient> ingredients;
    private Map<Long, Category> categories;
    private Map<Long, Product> products;
    private Map<Long, ProductPrice> prices;

    private Map<Category, Category> category_parent;
    private Map<ProductPrice, Product> price_product;
    private Map<Product, Ingredient> product_ingredient;
    private Map<Product, Category> product_category;

    @Autowired
    public CatalogImporter(@Qualifier("pizzaWithIngredients") Resource[] resources,
                           DataExtractor dataExtractor, Loader loader) {
        this.resources = resources;
        this.dataExtractor = dataExtractor;
        this.loader = loader;

        ingredients = new HashMap<>();
        categories = new HashMap<>();
        products = new HashMap<>();
        prices = new HashMap<>();
        category_parent = new HashMap<>();
        price_product = new HashMap<>();
        product_ingredient = new HashMap<>();
        product_category = new HashMap<>();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        LOGGER.info("Starting import of pizza-with-ingredients csvLines...");
        this.warnings.clear();

        List<CsvLine> csvLines = dataExtractor.extract(resources);
        updateOrPersistEntities(csvLines);
    }


    public void updateOrPersistEntities(List<CsvLine> csvLines) {
        LOGGER.info(String.format("Converting %s csvLines...", csvLines.size()));
        this.warnings.clear();

        // TODO: pizzasaus (+ contraint), aantalkeer_ingredient, beschikbaar

        for (var line : csvLines) {
            try {
                updateOrPersistEntities(line);
            } catch (Exception e) {
                processWarning(String.format("Invalid data in line: %s || ERROR: %s",
                        line.toString(), e.toString()));
                throw e; // dev
            }
        }
        updateRelationships();
    }

    private void updateOrPersistEntities(CsvLine l) {
        final double TAXRATE = 0.06; // TODO: user input?
        final Date FROMDATE = new Date();

        Product product = new Product(null, l.getProductName(), l.getProductDescription(),
                l.isSpicy(), l.isVegetarian(), l.getDeliveryFee(), TAXRATE, null);
        Category mainCat = new Category(null, null, l.getCategoryName());
        Category subCat = new Category(null, null, l.getSubCategoryName());
        Ingredient ingredient = new Ingredient(null, l.getIngredientName(), null);
        ProductPrice price = new ProductPrice(null, product, l.getPrice(), FROMDATE);

        if (!categories.containsKey(mainCat.getCatid())) {
            mainCat = loader.toDb(mainCat);
            categories.put(mainCat.getCatid(), mainCat);
        }

        if (!categories.containsKey(subCat.getCatid())) {
            subCat = loader.toDb(subCat);
            categories.put(subCat.getCatid(), subCat);
            category_parent.put(subCat, categories.get(mainCat.getCatid()));
        }

        if (!products.containsKey(product.getProductid())) {
            product = loader.toDb(product);
            products.put(product.getProductid(), product);
            product_category.put(product, categories.get(mainCat.getCatid()));
            product_category.put(product, categories.get(subCat.getCatid()));
        }

        if (!ingredients.containsKey(ingredient.getIngredientid())) {
            ingredient = loader.toDb(ingredient);
            ingredients.put(ingredient.getIngredientid(), ingredient);
            product_ingredient.put(products.get(product.getProductid()), ingredient);
        }

        if (!prices.containsKey(price.getPriceid())) {
            price = loader.toDb(price, product);
            prices.put(price.getPriceid(), price);
            price_product.put(price, products.get(product.getProductid()));
        }
    }


    private void updateRelationships() {
        for (Map.Entry<Category, Category> entry : category_parent.entrySet()) {
            entry.getKey().setParent(entry.getValue());
        }
        for (Map.Entry<ProductPrice, Product> entry : price_product.entrySet()) {
            entry.getKey().setProduct(entry.getValue());
        }
        for (Map.Entry<Product, Ingredient> entry : product_ingredient.entrySet()) {
            entry.getKey().addIngredient(entry.getValue());
        }
        for (Map.Entry<Product, Category> entry : product_category.entrySet()) {
            entry.getKey().addCategory(entry.getValue());
        }
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
