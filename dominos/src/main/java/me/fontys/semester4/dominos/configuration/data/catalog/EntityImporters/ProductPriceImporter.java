package me.fontys.semester4.dominos.configuration.data.catalog.EntityImporters;

import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
public class ProductPriceImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<ProductPrice> buffer;
    private final ProductPriceRepository repository;

    public ProductPriceImporter(ProductPriceRepository repository) {
        this.repository = repository;
        this.buffer = new ArrayList<>();
    }

    @Transactional
    public ProductPrice importPrice(PizzaAndIngredientRecord record, Product product) {
        ProductPrice price = getPrice(record, product);
        this.repository.save(price);

        return price;
    }

    private ProductPrice getPrice(PizzaAndIngredientRecord record, Product product) {
        if (record.getPrice().isEmpty()) {
            processWarning("Record does not have a product price");
            return null;
        }
        if (existsInBuffer(record.getPrice(), product)) {
            processWarning("Product price already processed");
            return null;
        }

        // query db
        ProductPrice price;
        try (Stream<ProductPrice> stream = this.repository
                .findByProductAndPrice(product.getProductid(), record.getPrice()))
        {
            if (stream.count() > 0) {
                price = stream.findFirst().get();
                // no props to set
            } else {
                price = new ProductPrice(
                        null,
                        product,
                        record.getPrice(),
                        new Date()
                );
            }
        }
        buffer.add(price);

        return price;
    }

    private boolean existsInBuffer(String priceStr, Product product) {
        for (var price : buffer) {
            if (price.getPriceid().toString().equals(priceStr)) {
                if (price.getProduct().getProductid().equals(product.getProductid())) {
                    // cannot compare dates
                    return true;
                }
            }
        }
        return false;
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Imported %s product prices", this.buffer.size()));

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Product price import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
