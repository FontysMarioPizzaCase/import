package me.fontys.semester4.dominos.configuration.data.catalog.dataloader;

import me.fontys.semester4.data.entity.*;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasDatabaseLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DatabaseLoader implements HasDatabaseLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DatabaseLoader.class);
    protected final DatabaseLogger log;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductPriceRepository productPriceRepository;

    public DatabaseLoader(DatabaseLoggerFactory databaseLoggerFactory, ProductRepository productRepository, CategoryRepository categoryRepository,
                          IngredientRepository ingredientRepository, ProductPriceRepository productPriceRepository) {
        this.log = databaseLoggerFactory.extendedLogger(LOGGER);
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.productPriceRepository = productPriceRepository;
    }

    // Not SOLID. Refactor wanted.
    public Ingredient toDb(Ingredient ingredient) {
        Optional<Ingredient> temp = this.ingredientRepository.findByNameIgnoreCase(ingredient.getName());

        if (temp.isPresent()) {
            Ingredient newData = ingredient;
            ingredient = temp.get();
            updateIngredient(ingredient, newData);
            log.addToReport("Ingredient updated", LogLevel.INFO);
        } else {
            this.ingredientRepository.save(ingredient);
            log.addToReport("Ingredient saved", LogLevel.INFO);
        }

        return ingredient;
    }

    private void updateIngredient(Ingredient ingredient, Ingredient newData) {
        ingredient.setName(newData.getName());
        ingredient.setDescription(newData.getDescription());
        ingredient.setSize(newData.getSize());
        ingredient.setAddprice(newData.getAddprice());
        ingredient.setAvailable(newData.isAvailable());
        ingredient.setAddprice(newData.getAddprice());
    }

    public Category toDb(Category category) {
        Optional<Category> temp = this.categoryRepository.findByNameIgnoreCase(category.getName());

        if (temp.isPresent()) {
            category = temp.get();
            log.addToReport("No Category properties to update", LogLevel.INFO);
        } else {
            this.categoryRepository.save(category);
            log.addToReport("Category created", LogLevel.INFO);
        }

        return category;
    }

    @Transactional
    public ProductPrice toDb(ProductPrice price, Product product) {
        Optional<ProductPrice> temp;

        try (Stream<ProductPrice> stream = this.productPriceRepository
                .findByPriceAndProduct_Productid(price.getPrice(), product.getProductid())) {
            temp = stream.findFirst();
        } catch (Exception e) {
            log.addToReport(
                    String.format("Could not query db for ProductPrice: %s", e.toString()),
                    LogLevel.ERROR);
            throw e;
        }

        if (temp.isPresent()) {
            price = temp.get();
            log.addToReport("No ProductPrice properties to update", LogLevel.INFO);
        } else {
            this.productPriceRepository.save(price);
            log.addToReport("ProductPrice created", LogLevel.INFO);
        }

        return price;
    }

    public Product toDb(Product product) {
        Optional<Product> temp = this.productRepository.findByNameIgnoreCase(product.getName());

        if (temp.isPresent()) {
            Product newData = product;
            product = temp.get();
            updateProduct(product, newData);
            log.addToReport("Product updated", LogLevel.INFO);
        } else {
            this.productRepository.save(product);
            log.addToReport("Product created", LogLevel.INFO);
        }

        return product;
    }

    private void updateProduct(Product product, Product newData) {
        product.setName(newData.getName());
        product.setDescription(newData.getDescription());
        product.setSpicy(newData.getSpicy());
        product.setVegetarian(newData.getVegetarian());
        product.setTaxrate(newData.getTaxrate());
        product.setImagepath(newData.getImagepath());
    }

    @Override
    public void report() {
        log.report();
    }

    public void clearWarnings() {
        log.clearReport();
    }
}
