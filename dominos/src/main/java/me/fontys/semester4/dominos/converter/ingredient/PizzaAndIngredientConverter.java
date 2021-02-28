package me.fontys.semester4.dominos.converter.ingredient;

import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PizzaAndIngredientConverter {
    private List<PizzaAndIngredientRecord> records;
    private final List<Ingredient> ingredients;
    private final List<Product> products;
    private final List<ProductPrice> prices;
    private final List<Category> categories;

    public PizzaAndIngredientConverter(String fileName, char separator) throws FileNotFoundException {
        this.records = new ArrayList<>();
        this.ingredients = new ArrayList<>();
        this.products = new ArrayList<>();
        this.prices = new ArrayList<>();
        this.categories = new ArrayList<>();

        loadRecords(fileName, separator);
        records.forEach(System.out::println);

        startExtract();

        // validate();
    }

    private void loadRecords(String fileName, char separator) throws FileNotFoundException {
        records = new CsvToBeanBuilder(new FileReader(fileName))
                .withSeparator(separator)
                .withType(PizzaAndIngredientRecord.class)
                .build()
                .parse();
    }

    private void startExtract() {
        for (var record : records) {
            if (hasNewProduct(record)) {
                extractProduct(record);
            }
            if (hasNewIngredient(record)) {
                extractIngredient(record);
            }
            if (hasNewCategory(record)) {
                extractCategory(record);
            }
            if (hasNewSubCategory(record)) {
                extractSubCategory(record);
            }
            extractPrice(record);
        }
    }

    private boolean hasNewProduct(PizzaAndIngredientRecord record) {
        for (var product : products) {
            if (record.getProductnaam().equals(product.getName())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNewIngredient(PizzaAndIngredientRecord record) {
        for (var ingredient : ingredients) {
            if (record.getIngredientnaam().equals(ingredient.getName())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNewCategory(PizzaAndIngredientRecord record) {
        for (var category : categories) {
            if (record.getCategorie().equals(category.getName())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNewSubCategory(PizzaAndIngredientRecord record) {
        for (var category : categories) {
            if (record.getSubcategorie().equals(category.getName())) {
                return false;
            }
        }
        return true;
    }

    // record.getCategorie();
    // record.getSubcategorie();
    //x record.getProductnaam();
    //x record.getProductomschrijving();
    //x record.getPrijs();
    // record.getBezorgtoeslag();
    // record.getSpicy();
    // record.getVegetarisch();
    // record.getBeschikbaar();
    // record.getAantalkeer_ingredient();
    //x record.getIngredientnaam();
    // record.getPizzasaus_standaard();

    private void extractProduct(PizzaAndIngredientRecord record) {
        products.add(new Product(
                0L,
                record.getProductnaam(),
                record.getProductomschrijving(),
                record.getSpicy().equals("Ja") ? true : false,
                record.getVegetarisch().equals("ja") ? true : false,
                new BigDecimal(record.getBezorgtoeslag()),
                0.06,
                null
        ));
    }

    private void extractIngredient(PizzaAndIngredientRecord record) {
        ingredients.add(new Ingredient(
                0L,
                record.getIngredientnaam(),
                null
        ));
    }

    private void extractCategory(PizzaAndIngredientRecord record) {
        categories.add(new Category(
                null,
                null,
                record.getCategorie()
        ));
    }

    private void extractSubCategory(PizzaAndIngredientRecord record) {
        Category parent = categories.stream()
                .filter(cat -> cat.getName().equals(record.getCategorie()))
                .findAny().orElse(null); // TODO: fix null check
        categories.add(new Category(
                null,
                parent,
                record.getSubcategorie()
        ));
    }

    private void extractPrice(PizzaAndIngredientRecord record) {
        prices.add(new ProductPrice(
                null,
                null, // TODO:
                record.getPrijs(),
                new Date()
        ));
    }
}
