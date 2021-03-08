package me.fontys.semester4.dominos.configuration.data.catalog.entitymanagers;

import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.PizzaAndIngredientRecord;
import me.fontys.semester4.dominos.configuration.data.catalog.util.PriceCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ProductPriceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceManager.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<ProductPrice> buffer;
    private final ProductPriceRepository repository;
    private final PriceCleaner priceCleaner;

    public ProductPriceManager(ProductPriceRepository repository, PriceCleaner priceCleaner) {
        this.repository = repository;
        this.priceCleaner = priceCleaner;
        this.buffer = new ArrayList<>();
    }

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

        productPrice = saveNewOrUpdate(product, price);
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

    private ProductPrice saveNewOrUpdate(Product product, BigDecimal price) {
        ProductPrice productPrice;
        Optional<ProductPrice> temp;

        try (Stream<ProductPrice> stream = this.repository
                .findByPriceAndProduct_Productid(price.toString(), product.getProductid())) {
            temp = stream.findFirst();
        } catch (Exception e) {
            processWarning(String.format("Could not query db for ProductPrice: %s", e.toString()));
            throw e;
        }

        if (temp.isPresent()) {
            productPrice = temp.get();
            processWarning("No ProductPrice properties to update");
        } else {
            productPrice = saveNewProductPrice(price, product);
            processWarning("ProductPrice created");
        }

        return productPrice;
    }

    private ProductPrice saveNewProductPrice(BigDecimal price, Product product) {
        ProductPrice productPrice = new ProductPrice(
                null,
                product,
                price,
                new Date()
        );
        this.repository.save(productPrice);
        return productPrice;
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
