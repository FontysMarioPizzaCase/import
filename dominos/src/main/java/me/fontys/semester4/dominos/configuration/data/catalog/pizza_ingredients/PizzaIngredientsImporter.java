package me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.general.CsvImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.general.DataExtractorFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.general.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.general.helper_models.Relationship;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
public class PizzaIngredientsImporter extends CsvImporter<PizzaIngredientsRawCsvLine, PizzaIngredientsCsvLine> {

    private final Map<Long, Ingredient> ingredients;
    private final Map<Long, Category> categories;
    private final Map<Long, Product> products;
    private final Map<Long, ProductPrice> prices;

    private final Set<Relationship<Category, Category>> category_parent;
    private final Set<Relationship<ProductPrice, Product>> price_product;
    private final Set<Relationship<Product, Ingredient>> product_ingredient;
    private final Set<Relationship<Product, Category>> product_category;
    private final Set<Relationship<Ingredient, Category>> ingredient_category;

    @Autowired
    public PizzaIngredientsImporter(Environment environment,
                                    ExtendedLoggerFactory extendedLoggerFactory,
                                    @Qualifier("pizzaWithIngredients") Resource[] resources,
                                    DataExtractorFactory dataExtractorFactory,
                                    PizzaIngredientsDataValidator validator, PizzaIngredientsDataCleaner cleaner,
                                    DatabaseLoader loader) {
        super(environment, extendedLoggerFactory, resources,
                dataExtractorFactory.getPizzaIngredientsDataExtractor(),
                validator, cleaner, loader);
        ingredients = new HashMap<>();
        categories = new HashMap<>();
        products = new HashMap<>();
        prices = new HashMap<>();
        category_parent = new HashSet<>();
        price_product = new HashSet<>();
        product_ingredient = new HashSet<>();
        product_category = new HashSet<>();
        ingredient_category = new HashSet<>();
    }

    @Override
    protected void doImport(List<PizzaIngredientsCsvLine> csvLines) {
        super.doImport(csvLines); // calls transformAndLoad below
        loadCachedRelationships();
    }

    @Override
    protected void transformAndLoad(PizzaIngredientsCsvLine l) {
        final String INGREDIENTCATEGORYNAME = environment.getProperty(
                "catalog.pizzaingredientsimport.default_category_for_ingredients");
        final String SAUCECATEGORYNAME = environment.getProperty(
                "catalog.pizzaingredientsimport.default_category_for_sauce");
        final double TAXRATE =
                Double.parseDouble(Objects.requireNonNull(environment.getProperty(
                "catalog.pizzaingredientsimport.default_taxrate_for_products")));
        Date FROMDATE;
        try {
            SimpleDateFormat dateFormatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            FROMDATE = dateFormatter.parse(environment.getProperty(
                    "catalog.pizzaingredientsimport.default_fromdate_for_price"));
        } catch (ParseException e) {
            FROMDATE = new Date();
        }

        Product product = new Product(null, l.getProductName(), l.getProductDescription(),
                l.isSpicy(), l.isVegetarian(), l.isAvailable(), TAXRATE, null);
        Category mainCat = new Category(null, null, l.getCategoryName());
        Category subCat = new Category(null, null, l.getSubCategoryName());
        Ingredient ingredient = new Ingredient(null, l.getIngredientName(), null, null,
                null, true);
        Ingredient sauce = new Ingredient(null, l.getStandardPizzasauce(), null, null,
                null, true);
        Category ingrCat = new Category(null, null, INGREDIENTCATEGORYNAME);
        Category sauceCat = new Category(null, null, SAUCECATEGORYNAME);
        ProductPrice price = new ProductPrice(null, product, l.getPrice(), FROMDATE);

        // save main category
        mainCat = loader.toDb(mainCat);
        categories.put(mainCat.getCatid(), mainCat);

        // save subcategory and cache relationship to parent
        subCat = loader.toDb(subCat);
        categories.put(subCat.getCatid(), subCat);
        category_parent.add(new Relationship<>(subCat, categories.get(mainCat.getCatid())));

        // save ingredient categories
        ingrCat = loader.toDb(ingrCat);
        sauceCat = loader.toDb(sauceCat);
        categories.put(ingrCat.getCatid(), ingrCat);
        categories.put(sauceCat.getCatid(), sauceCat);

        // save product and cache relationship to categories
        product = loader.toDb(product);
        products.put(product.getProductid(), product);
        product_category.add(new Relationship<>(product, categories.get(mainCat.getCatid())));
        product_category.add(new Relationship<>(product, categories.get(subCat.getCatid())));

        // save ingredients and cache relationships to product and relationships to categories
        ingredient = loader.toDb(ingredient);
        ingredients.put(ingredient.getIngredientid(), ingredient);
        product_ingredient.add(new Relationship<>(products.get(product.getProductid()), ingredient));
        ingredient_category.add(new Relationship<>(ingredient, categories.get(ingrCat.getCatid())));

        sauce = loader.toDb(sauce);
        ingredients.put(sauce.getIngredientid(), sauce);
        product_ingredient.add(new Relationship<>(products.get(product.getProductid()), sauce));
        ingredient_category.add(new Relationship<>(sauce, categories.get(sauceCat.getCatid())));

        // save price and cache relationship to product
        price = loader.toDb(price, product);
        prices.put(price.getPriceid(), price);
        price_product.add(new Relationship<>(price, products.get(product.getProductid())));
    }

    protected void loadCachedRelationships() {
        for (Relationship<Category, Category> relation : category_parent) {
            relation.getLeft().setParent(relation.getRight());
        }
        for (Relationship<ProductPrice, Product> relation : price_product) {
            relation.getLeft().setProduct(relation.getRight());
        }
        for (Relationship<Product, Ingredient> relation : product_ingredient) {
            relation.getLeft().addIngredient(relation.getRight());
        }
        for (Relationship<Product, Category> relation : product_category) {
            relation.getLeft().addCategory(relation.getRight());
        }
        for (Relationship<Ingredient, Category> relation : ingredient_category) {
            relation.getLeft().addCategory(relation.getRight());
        } // TODO: consolidate sauce and ingredient category changes with overige producten
    }
}
