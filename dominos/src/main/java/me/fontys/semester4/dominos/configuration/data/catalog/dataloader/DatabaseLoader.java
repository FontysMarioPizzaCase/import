package me.fontys.semester4.dominos.configuration.data.catalog.dataloader;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DatabaseLoader implements HasExtendedLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DatabaseLoader.class);
    protected final ExtendedLogger extendedLogger;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductPriceRepository productPriceRepository;

    public DatabaseLoader(ExtendedLoggerFactory extendedLoggerFactory, ProductRepository productRepository, CategoryRepository categoryRepository,
                          IngredientRepository ingredientRepository, ProductPriceRepository productPriceRepository) {
        this.extendedLogger = extendedLoggerFactory.extendedLogger(LOGGER);
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
            extendedLogger.processWarning("Ingredient updated");
        } else {
            this.ingredientRepository.save(ingredient);
            extendedLogger.processWarning("Ingredient saved");
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
            extendedLogger.processWarning("No Category properties to update");
        } else {
            this.categoryRepository.save(category);
            extendedLogger.processWarning("Category created");
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
            extendedLogger.processWarning(String.format("Could not query db for ProductPrice: %s", e.toString()));
            throw e;
        }

        if (temp.isPresent()) {
            price = temp.get();
            extendedLogger.processWarning("No ProductPrice properties to update");
        } else {
            this.productPriceRepository.save(price);
            extendedLogger.processWarning("ProductPrice created");
        }

        return price;
    }

    public Product toDb(Product product) {
        Optional<Product> temp = this.productRepository.findByNameIgnoreCase(product.getName());

        if (temp.isPresent()) {
            Product newData = product;
            product = temp.get();
            updateProduct(product, newData);
            extendedLogger.processWarning("Product updated");
        } else {
            this.productRepository.save(product);
            extendedLogger.processWarning("Product created");
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
        extendedLogger.report();
    }

    public void clearWarnings() {
        extendedLogger.clearWarnings();
    }
}
