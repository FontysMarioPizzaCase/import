package me.fontys.semester4.dominos.configuration.data.catalog.transform;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.CatalogImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.extract.CsvLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Transformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogImporter.class);
    private final Map<String, Integer> warnings = new HashMap<>();

    private List<Ingredient> ingredients;
    private List<Category> categories;
    private List<Product> products;
    private List<ProductPrice> prices;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public List<Category> getCategories() {
        return categories;
    }
    public List<Product> getProducts() {
        return products;
    }
    public List<ProductPrice> getPrices() {
        return prices;
    }


    public Transformer(){
        ingredients = new ArrayList<>();
        categories = new ArrayList<>();
        products = new ArrayList<>();
        prices = new ArrayList<>();
    }

    public void toEntities(List<CsvLine> csvLines) {
        LOGGER.info(String.format("Converting %s csvLines...", csvLines.size()));
        this.warnings.clear();

        for (var line : csvLines) {
            try {
                transform(line);
            } catch (Exception e) {
                processWarning(String.format("Invalid data in line: %s || ERROR: %s",
                        line.toString(), e.toString()));
            }

            // TODO: pizzasaus (+ contraint), aantalkeer_ingredient, beschikbaar
        }
    }

    private void transform(CsvLine l) {
        final double TAXRATE = 0.06; // TODO: user input?
        final Date FROMDATE = new Date();

        Product product = new Product(null, l.getProductName(), l.getProductDescription(),
                l.isSpicy(), l.isVegetarian(), l.getDeliveryFee(), TAXRATE, null);
        Category parent = new Category(null, null, l.getCategoryName());
        Category child = new Category(null, parent, l.getSubCategoryName());
        Ingredient ingredient = new Ingredient(null, l.getIngredientName(), null);
        ProductPrice price = new ProductPrice(null, product, l.getPrice(), FROMDATE);

        products.add(product);
        categories.add(parent);
        categories.add(child);
        ingredients.add(ingredient);
        prices.add(price);

        product.getIngredients().add(ingredient);
        product.getCategories().add(parent);
        product.getCategories().add(child);
    }




    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
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
