package me.fontys.semester4.dominos.configuration.data.catalog.EntityImporters;

import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import me.fontys.semester4.dominos.configuration.data.catalog.Util.PriceCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ProductPriceImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<ProductPrice> buffer;
    private final ProductPriceRepository repository;
    private final PriceCleaner priceCleaner;

    public ProductPriceImporter(ProductPriceRepository repository, PriceCleaner priceCleaner) {
        this.repository = repository;
        this.priceCleaner = priceCleaner;
        this.buffer = new ArrayList<>();
    }

    @Transactional
    public ProductPrice extractAndImport(PizzaAndIngredientRecord record, Product product) {
        if (record.getPrice().isEmpty()) {
            processWarning("Record does not have a product price");
            throw new IllegalArgumentException();
        }

        BigDecimal price = priceCleaner.clean(record.getPrice());
        ProductPrice productPrice = findInBuffer(price, product.getProductid());

        if (productPrice != null) {
            processWarning("Product price already processed. Skipped.");
            return productPrice;
        }

        // query db
        try (Stream<ProductPrice> stream = this.repository
                .findByPriceAndProduct_Productid(price.toString(), product.getProductid())) {

            Optional<ProductPrice> temp = stream.findFirst();

            if (temp.isPresent()) {
                productPrice = temp.get();
                // no props to set
                processWarning("Price updated");
            } else {
                productPrice = new ProductPrice(
                        null,
                        product,
                        price,
                        new Date()
                );
                this.repository.save(productPrice);
                processWarning("Price created");
            }
        } catch (Exception e) {
            processWarning(String.format("Could not query db for ProductPrice: %s", e.toString()));
            throw e;
        }

        buffer.add(productPrice);

        return productPrice;
    }

    private ProductPrice findInBuffer(BigDecimal price, Long productId) {
        for (var productPrice : buffer) {
            if (price.equals(productPrice.getPrice())) {
                if (productId.equals(productPrice.getProduct().getProductid())) {
                    // cannot compare dates
                    return productPrice;
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
