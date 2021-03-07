package me.fontys.semester4.dominos.configuration.data.catalog.EntityImporters;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Product> buffer;
    private final ProductRepository repository;

    public ProductImporter(ProductRepository repository) {
        this.repository = repository;
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

        // query db
        Optional<Product> temp = this.repository.findByName(record.getProductName());

        if(temp.isPresent()){
            product = temp.get();
            product.setName(record.getProductName());
            product.setDescription(record.getProductDescription());
            product.setSpicy(record.getIsSpicy().equalsIgnoreCase("JA"));
            product.setVegetarian(record.getIsVegetarian().equalsIgnoreCase("JA"));
            // TODO: validate delivery fee
            product.setDeliveryfee(BigDecimal.TEN);
            processWarning("Product updated");
        }
        else {
            product = new Product(
                    null,
                    record.getProductName(),
                    record.getProductDescription(),
                    record.getIsSpicy().equalsIgnoreCase("JA"),
                    record.getIsVegetarian().equalsIgnoreCase("JA"),
                    // TODO: validate delivery fee
                    // new BigDecimal(record.getBezorgtoeslag()),
                    BigDecimal.TEN,
                    0.06,
                    null
            );
            this.repository.save(product);
            processWarning("Product created");
        }

        // add relationships
        product.getIngredients().add(ingredient);
        product.getCategories().addAll(categories);

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
