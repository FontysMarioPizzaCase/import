package me.fontys.semester4.dominos.configuration.data.catalog.Converters;

import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Product> products;
    private final ProductRepository repository;

    public List<Product> getProducts() {
        return products;
    }

    public ProductImporter(ProductRepository repository) {
        this.repository = repository;
        this.products = new ArrayList<>();
    }

    public Product process(PizzaAndIngredientRecord record) {
        if (record.getProductName().isEmpty()) {
            processWarning("Record does not have a product name");
            return null;
        }
        if (existsByName(record.getProductName())) {
            processWarning("Product already processed");
            return null;
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

        Product newProduct = new Product(
                null,
                record.getProductName(),
                record.getProductDescription(),
                record.getIsSpicy().equalsIgnoreCase("JA"),
                record.getIsVegetarian().equalsIgnoreCase("JA"),
                // TODO: validate price
                // new BigDecimal(record.getBezorgtoeslag()),
                BigDecimal.TEN,
                0.06,
                null
        );
        products.add(newProduct);

        return newProduct;
    }


    private boolean existsByName(String productName) {
        for (var product : products) {
            if (productName.equals(product.getName())) {
                return true;
            }
        }
        return false;
    }

    public void saveAll() {
        this.repository.saveAll(this.products);
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Converted %s products", this.products.size()));

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Product conversion result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
