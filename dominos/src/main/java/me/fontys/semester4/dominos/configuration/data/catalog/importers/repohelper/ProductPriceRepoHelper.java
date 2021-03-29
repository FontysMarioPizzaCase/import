package me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper;

import me.fontys.semester4.data.entity.*;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;

import java.util.Optional;
import java.util.stream.Stream;

public class ProductPriceRepoHelper {
    protected final DatabaseLogger<LogEntry> log;
    private final ProductPriceRepository priceRepository;

    public ProductPriceRepoHelper(DatabaseLogger<LogEntry> databaseLogger, ProductPriceRepository priceRepository) {
        this.log = databaseLogger;
        this.priceRepository = priceRepository;
    }

    public ProductPrice saveOrUpdate(ProductPrice newData, Product product) {
        Optional<ProductPrice> temp;

        try (Stream<ProductPrice> stream = this.priceRepository
                .findByPriceAndProduct_ProductidAndFromdate(newData.getPrice(),
                        product.getProductid(), newData.getFromdate())) {
            temp = stream.findFirst();
        } catch (Exception e) {
            log.addToReport(
                    String.format("Could not query db for ProductPrice: %s", e.toString()),
                    Severity.ERROR);
            throw e; // FIXME (dev)
        }

        ProductPrice productPrice;
        if (temp.isPresent()) {
            productPrice = temp.get();
            updateProperties(productPrice, newData);
        } else {
            productPrice = newData;
            saveEntity(productPrice);
        }

        return productPrice;
    }

    private void saveEntity(ProductPrice productPrice) {
        this.priceRepository.save(productPrice);
        log.addToReport("Created product price: " + productPrice, Severity.INFO);
    }

    private void updateProperties(ProductPrice productPrice, ProductPrice newData) {
        // placeholder, no properties to update
    }
}
