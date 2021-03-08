package me.fontys.semester4.dominos.configuration.data.catalog.EntityImporters;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import me.fontys.semester4.dominos.configuration.data.catalog.Util.PriceCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Product> buffer;
    private final ProductRepository repository;
    private final PriceCleaner priceCleaner;

    public ProductImporter(ProductRepository repository, PriceCleaner priceCleaner) {
        this.repository = repository;
        this.priceCleaner = priceCleaner;
        this.buffer = new ArrayList<>();
    }

    @Transactional
    public Product extractAndImport(PizzaAndIngredientRecord record,
                                    Ingredient ingredient, List<Category> categories) {
        Product product = findInBuffer(record.getProductName());

        if (product != null) {
            processWarning("Product already processed. Skipped.");
            return product;
        }
        if (record.getProductName().isEmpty()) {
            processWarning("Record does not have a product name");
            throw new IllegalArgumentException();
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

        product = saveNewOrUpdate(record, ingredient, categories);
        buffer.add(product);

        return product;
    }

    private Product findInBuffer(String productName) {
        for (var product : buffer) {
            if (productName.equals(product.getName())) {
                return product;
            }
        }
        return null;
    }

    private Product saveNewOrUpdate(PizzaAndIngredientRecord record, Ingredient ingredient, List<Category> categories) {
        Product product;
        Optional<Product> temp = this.repository.findByName(record.getProductName());

        if(temp.isPresent()){
            product = temp.get();
            product = updateProduct(product, record);
            processWarning("Product updated");
        }
        else {
            product = saveNewProduct(record);
            processWarning("Product created");
        }

        addRelationships(product, ingredient, categories);

        return product;
    }

    private Product updateProduct(Product product, PizzaAndIngredientRecord record) {
        product.setName(record.getProductName());
        product.setDescription(record.getProductDescription());
        product.setSpicy(record.getIsSpicy().equalsIgnoreCase("JA"));
        product.setVegetarian(record.getIsVegetarian().equalsIgnoreCase("JA"));
        product.setDeliveryfee(priceCleaner.clean(record.getDeliveryFee()));

        return product;
    }

    private Product saveNewProduct(PizzaAndIngredientRecord record) {
        Product product = new Product(
                null,
                record.getProductName(),
                record.getProductDescription(),
                record.getIsSpicy().equalsIgnoreCase("JA"),
                record.getIsVegetarian().equalsIgnoreCase("JA"),
                priceCleaner.clean(record.getDeliveryFee()),
                // TODO: input taxrate?
                0.06,
                null
        );
        this.repository.save(product);

        return product;
    }

    private void addRelationships(Product product, Ingredient ingredient, List<Category> categories) {
        product.getIngredients().add(ingredient);
        product.getCategories().addAll(categories);
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Added or updated %s products", this.buffer.size()));

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Product import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
