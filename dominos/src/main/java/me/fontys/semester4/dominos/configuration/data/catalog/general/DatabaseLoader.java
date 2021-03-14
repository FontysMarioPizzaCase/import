package me.fontys.semester4.dominos.configuration.data.catalog.general;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.util.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    // TODO: refactor wanted?? Generic is messy cause toDB methods are not quite the same
    public Ingredient toDb(Ingredient ingredient) {
        Optional<Ingredient> temp = this.ingredientRepository.findByNameIgnoreCase(ingredient.getName());

        if (temp.isPresent()) {
            Ingredient newData = ingredient;
            ingredient = temp.get();
            if(newData.getAddprice() != null) {
                ingredient.setAddprice(newData.getAddprice());
                extendedLogger.processWarning("Ingredient surcharge updated");
            }
            else extendedLogger.processWarning("No Ingredient properties to update");
        } else {
            this.ingredientRepository.save(ingredient);
            extendedLogger.processWarning("Ingredient saved");
        }

        return ingredient;
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

    private void updateProduct(Product dbProduct, Product p) {
        dbProduct.setName(p.getName());
        dbProduct.setDescription(p.getDescription());
        dbProduct.setSpicy(p.getSpicy());
        dbProduct.setVegetarian(p.getVegetarian());
        dbProduct.setTaxrate(p.getTaxrate());
        dbProduct.setImagepath(p.getImagepath());
    }

    @Override
    public void report() {
        extendedLogger.report();
    }

    public void clearWarnings() {
        extendedLogger.clearWarnings();
    }
}
