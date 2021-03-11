package me.fontys.semester4.dominos.configuration.data.catalog.load;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.CatalogImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class Loader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogImporter.class);
    private final Map<String, Integer> warnings = new HashMap<>();

    private ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductPriceRepository productPriceRepository;

    public Loader(ProductRepository productRepository, CategoryRepository categoryRepository,
                  IngredientRepository ingredientRepository, ProductPriceRepository productPriceRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.productPriceRepository = productPriceRepository;
    }


    // TODO: refactor wanted??
    public Ingredient toDb(Ingredient ingredient) {
        Optional<Ingredient> temp = this.ingredientRepository.findByNameIgnoreCase(ingredient.getName());

        if (temp.isPresent()) {
            ingredient = temp.get();
            processWarning("No Ingredient properties to update");
        } else {
            this.ingredientRepository.save(ingredient);
            processWarning("Ingredient saved");
        }

        return ingredient;
    }

    public Category toDb(Category category) {
        Optional<Category> temp = this.categoryRepository.findByNameIgnoreCase(category.getName());

        if (temp.isPresent()) {
            category = temp.get();
            processWarning("No Category properties to update");
        } else {
            this.categoryRepository.save(category);
            processWarning("Category created");
        }

        return category;
    }

    public ProductPrice toDb(ProductPrice price, Product product) {
        Optional<ProductPrice> temp;

        try (Stream<ProductPrice> stream = this.productPriceRepository
                .findByPriceAndProduct_Productid(price.getPrice(), product.getProductid())) {
            temp = stream.findFirst();
        } catch (Exception e) {
            processWarning(String.format("Could not query db for ProductPrice: %s", e.toString()));
            throw e;
        }

        if (temp.isPresent()) {
            price = temp.get();
            processWarning("No ProductPrice properties to update");
        } else {
            this.productPriceRepository.save(price);
            processWarning("ProductPrice created");
        }

        return price;
    }

    public Product toDb(Product product) {
        Optional<Product> temp = this.productRepository.findByNameIgnoreCase(product.getName());

        if (temp.isPresent()) {
            Product newData = product;
            product = temp.get();
            updateProduct(product, newData);
            processWarning("Product updated");
        } else {
            this.productRepository.save(product);
            processWarning("Product created");
        }

        return product;
    }

    private void updateProduct(Product dbProduct, Product p) {
        dbProduct.setName(p.getName());
        dbProduct.setDescription(p.getDescription());
        dbProduct.setSpicy(p.getSpicy());
        dbProduct.setVegetarian(p.getVegetarian());
        dbProduct.setDeliveryfee(p.getDeliveryfee());
        dbProduct.setTaxrate(p.getTaxrate());
        dbProduct.setImagepath(p.getImagepath());
//        dbProduct.setCategories(p.getCategories());
//        dbProduct.setIngredients(p.getIngredients());
//        dbProduct.setPrices(p.getPrices());
    }


    // REFACTOR ABOVE


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
