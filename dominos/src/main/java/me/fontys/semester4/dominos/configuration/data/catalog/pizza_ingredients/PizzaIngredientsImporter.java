package me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.general.CatalogImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.general.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class PizzaIngredientsImporter extends CatalogImporter<PizzaIngredientsRawCsvLine, PizzaIngredientsCsvLine> {

    private final Map<Long, Ingredient> ingredients;
    private final Map<Long, Category> categories;
    private final Map<Long, Product> products;
    private final Map<Long, ProductPrice> prices;

    private final Map<Category, Category> category_parent;
    private final Map<ProductPrice, Product> price_product;
    private final Map<Product, Ingredient> product_ingredient;
    private final Map<Product, Category> product_category;


    @Autowired
    public PizzaIngredientsImporter(ExtendedLoggerFactory extendedLoggerFactory,
                                    @Qualifier("pizzaWithIngredients") Resource[] resources,
                                    PizzaIngredientsDataPrepper pizzaIngredientsDataExtractor,
                                    DatabaseLoader loader) {
        super(extendedLoggerFactory, resources, pizzaIngredientsDataExtractor, loader);
        ingredients = new HashMap<>();
        categories = new HashMap<>();
        products = new HashMap<>();
        prices = new HashMap<>();
        category_parent = new HashMap<>();
        price_product = new HashMap<>();
        product_ingredient = new HashMap<>();
        product_category = new HashMap<>();
    }

    @Override
    protected void doImport(List<PizzaIngredientsCsvLine> csvLines) {
        super.doImport(csvLines); // calls transformAndLoad below
        loadCachedRelationships();
    }

    @Override
    protected void transformAndLoad(PizzaIngredientsCsvLine l) {
        // TODO: pizzasaus (+ contraint), aantalkeer_ingredient

        final double TAXRATE = 0.06; // TODO: user input?
        final Date FROMDATE = new Date();

        Product product = new Product(null, l.getProductName(), l.getProductDescription(),
                l.isSpicy(), l.isVegetarian(), l.isAvailable(), TAXRATE, null);
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

    protected void loadCachedRelationships() {
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
}
