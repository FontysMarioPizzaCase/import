package me.fontys.semester4.dominos.pizza.and.ingredient;

import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PizzaAndIngredientConverter {
    private List<PizzaAndIngredientRecord> records;
    private final List<Ingredient> ingredients;
    private final List<Product> products;
    private final List<ProductPrice> prices;
    private final List<Category> categories;

    public PizzaAndIngredientConverter() {
        this.records = new ArrayList<>();
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

    public void convert(String fileName, char separator) throws FileNotFoundException {
        loadRecords(fileName, separator);
        extractAndUpdate();

        // TODO: pizzasaus, aantalkeer_ingredient, beschikbaar
        // TODO: validate();
    }

    private void loadRecords(String fileName, char separator) throws FileNotFoundException {
        records = new CsvToBeanBuilder(new FileReader(fileName))
                .withSeparator(separator)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .withType(PizzaAndIngredientRecord.class)
                .build()
                .parse();
    }

    private void extractAndUpdate() {
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
            Category subCategory = findCategory(newSubCategory.getName());
            product.getIngredients().add(ingredient);
            product.getCategories().add(subCategory);

        }
    }

    private Product extractProduct(PizzaAndIngredientRecord record) {
        return new Product(
                null,
                record.getProductnaam(),
                record.getProductomschrijving(),
                record.getSpicy().equals("Ja"),
                record.getVegetarisch().equals("ja"),
// TODO: validate price
//                new BigDecimal(record.getBezorgtoeslag()),
                BigDecimal.TEN,
                0.06,
                null
        );
    }

    private ProductPrice extractPrice(PizzaAndIngredientRecord record, Long productId) {
        return new ProductPrice(
                null,
                productId,
                record.getPrijs(),
                new Date()
        );
    }

    private Ingredient extractIngredient(PizzaAndIngredientRecord record) {
        return new Ingredient(
                null,
                record.getIngredientnaam(),
                null
        );
    }

    private Category extractCategory(PizzaAndIngredientRecord record) {
        return new Category(
                null,
                null,
                record.getCategorie()
        );
    }

    private Category extractSubCategory(PizzaAndIngredientRecord record) {
        Category parent = categories.stream()
                .filter(cat -> cat.getName().equals(record.getCategorie()))
                .findAny().orElse(null); // TODO: fix null check
        return new Category(
                null,
                parent,
                record.getSubcategorie()
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

    private Product findProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .findAny().orElse(null);
    }

    private Ingredient findIngredient(String ingredientName) {
        return ingredients.stream()
                .filter(p -> p.getName().equals(ingredientName))
                .findAny().orElse(null);
    }

    private Category findCategory(String categoryName) {
        return categories.stream()
                .filter(p -> p.getName().equals(categoryName))
                .findAny().orElse(null);
    }
}
