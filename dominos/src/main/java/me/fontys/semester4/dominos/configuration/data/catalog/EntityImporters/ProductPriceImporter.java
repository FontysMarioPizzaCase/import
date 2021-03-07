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
    public ProductPrice extractAndImport(PizzaAndIngredientRecord record, Product product) {
        // TODO: findInBuffer doesn't work
        ProductPrice price = findInBuffer(record.getPrice(), product);

        if (price != null) {
            processWarning("Product price already processed. Skipped.");
            return price;
        }
        if (record.getPrice().isEmpty()) {
            processWarning("Record does not have a product price");
            throw new IllegalArgumentException();
        }


        // query db
        try (Stream<ProductPrice> stream = this.repository
                // TODO: fix prices!
                // .findByProductAndPrice(product.getProductid(), record.getPrice()))
                .findByPriceAndProduct_Productid("66.66", product.getProductid()))
        {
            if (stream.count() > 0) {
                price = stream.findFirst().get();
                // no props to set
                processWarning("Price updated");
            } else {
                price = new ProductPrice(
                        null,
                        product,
                        record.getPrice(),
                        new Date()
                );
                this.repository.save(price);
                processWarning("Price created");
            }
        }
        buffer.add(price);

        return price;
    }

    private ProductPrice findInBuffer(String priceStr, Product product) {
        for (var price : buffer) {
            if (price.getPriceid().toString().equals(priceStr)) {
                if (price.getProduct().getProductid().equals(product.getProductid())) {
                    // cannot compare dates
                    return price;
                }
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
        LOGGER.info(String.format("Added or updated %s product prices", this.buffer.size()));

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
