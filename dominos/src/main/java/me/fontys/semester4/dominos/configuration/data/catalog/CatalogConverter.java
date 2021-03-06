package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CatalogConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogImporter.class);

    private final List<Ingredient> ingredients;
    private final List<Product> products;
    private final List<ProductPrice> prices;
    private final List<Category> categories;
    private final Map<String, Integer> warnings = new HashMap<>();


    public CatalogConverter() {
        this.ingredients = new ArrayList<>();
        this.products = new ArrayList<>();
        this.prices = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<ProductPrice> getPrices() {
        return prices;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void convert(List<PizzaAndIngredientRecord> records) {
        // TODO: pizzasaus, aantalkeer_ingredient, beschikbaar
        // TODO: validate();
        LOGGER.info(String.format("Converting %s records...", records.size()));

        for (var record : records) {
            // extract entities
            Product newProduct = extractProduct(record);
            ProductPrice newPrice = extractPrice(record, newProduct.getProductid());
            Ingredient newIngredient = extractIngredient(record);
            Category newCategory = extractCategory(record);
            Category newSubCategory = extractSubCategory(record);

            // update collections
            if (notInProducts(newProduct.getName())) {
                products.add(newProduct);
                prices.add(newPrice);
            }

            if (notInIngredients(newIngredient.getName())) {
                ingredients.add(newIngredient);
            }

            if (notInCategories(newSubCategory.getName())) {
                categories.add(newSubCategory);
            }

            if (notInCategories(newCategory.getName())) {
                categories.add(newCategory);
            }

            //update relationships
            Product product = findProduct(newProduct.getName());
            Ingredient ingredient = findIngredient(newIngredient.getName());
            Category category = findCategory(newCategory.getName());
            Category subCategory = findCategory(newSubCategory.getName());
            subCategory.setParent(category);
            product.getIngredients().add(ingredient);
            product.getCategories().add(subCategory);
        }

        LOGGER.info(String.format("Conversion result: "));
        LOGGER.info(String.format("- %s products", this.products.size()));
        LOGGER.info(String.format("- %s ingredients", this.ingredients.size()));
        LOGGER.info(String.format("- %s categories", this.categories.size()));
        LOGGER.info(String.format("- %s prices", this.prices.size()));
    }

    private Product extractProduct(PizzaAndIngredientRecord record) {
        if (record.getProductName().isEmpty()) {
            processWarning("Record does not have a product name");
        }
        if (record.getProductDescription().isEmpty()) {
            processWarning("Record does not have a product description");
        }
        if (record.getIsSpicy().isEmpty()) {
            processWarning("Record does not have a product spicy indicator");
        }
        if (record.getIsVegetarian().isEmpty()) {
            processWarning("Record does not have a product vegetarian indicator");
        }
        if (record.getDeliveryFee().isEmpty()) {
            processWarning("Record does not have a product delivery fee");
        }

        return new Product(
                null,
                record.getProductName(),
                record.getProductDescription(),
                record.getIsSpicy().equalsIgnoreCase("JA"),
                record.getIsVegetarian().equalsIgnoreCase("JA"),
// TODO: validate price
//                new BigDecimal(record.getBezorgtoeslag()),
                BigDecimal.TEN,
                0.06,
                null
        );
    }

    private ProductPrice extractPrice(PizzaAndIngredientRecord record, Long productId) {
        if (record.getPrice().isEmpty()) {
            processWarning("Record does not have a product price");
        }

        return new ProductPrice(
                null,
                productId,
                record.getPrice(),
                new Date()
        );
    }

    private Ingredient extractIngredient(PizzaAndIngredientRecord record) {
        if (record.getIngredientName().isEmpty()) {
            processWarning("Record does not have an ingredient name");
        }

        return new Ingredient(
                null,
                record.getIngredientName(),
                null
        );
    }

    private Category extractCategory(PizzaAndIngredientRecord record) {
        if (record.getCategory().isEmpty()) {
            processWarning("Record does not have a category name");
        }

        return new Category(
                null,
                null,
                record.getCategory()
        );
    }

    private Category extractSubCategory(PizzaAndIngredientRecord record) {
        if (record.getSubCategory().isEmpty()) {
            processWarning("Record does not have a subcategory name");
        }

        return new Category(
                null,
                null,
                record.getSubCategory()
        );
    }

    private boolean notInProducts(String productName) {
        for (var product : products) {
            if (productName.equals(product.getName())) {
                return false;
            }
        }
        return true;
    }

    private boolean notInIngredients(String ingredientName) {
        for (var ingredient : ingredients) {
            if (ingredientName.equals(ingredient.getName())) {
                return false;
            }
        }
        return true;
    }

    private boolean notInCategories(String categoryName) {
        for (var category : categories) {
            if (categoryName.equals(category.getName())) {
                return false;
            }
        }
        return true;
    }

    private Product findProduct(String productName) throws NoSuchElementException {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .findAny().orElseThrow();
    }

    private Ingredient findIngredient(String ingredientName) throws NoSuchElementException {
        return ingredients.stream()
                .filter(p -> p.getName().equals(ingredientName))
                .findAny().orElseThrow();
    }

    private Category findCategory(String categoryName) throws NoSuchElementException {
        return categories.stream()
                .filter(p -> p.getName().equals(categoryName))
                .findAny().orElseThrow();
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
            LOGGER.warn(String.format("Catalog conversion result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
