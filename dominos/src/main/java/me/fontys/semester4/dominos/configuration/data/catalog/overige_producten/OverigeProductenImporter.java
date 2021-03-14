package me.fontys.semester4.dominos.configuration.data.catalog.overige_producten;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.general.CatalogImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.general.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OverigeProductenImporter extends CatalogImporter<OverigProductRawCsvLine, OverigProductCsvLine> {
    private final Map<Long, Category> categories;
    private final Map<Long, Product> products;
    private final Map<Long, ProductPrice> prices;

    private final Map<Category, Category> category_parent;
    private final Map<ProductPrice, Product> price_product;
    private final Map<Product, Category> product_category;

    @Autowired
    public OverigeProductenImporter(ExtendedLoggerFactory extendedLoggerFactory,
                                    @Qualifier("overigeProducten") Resource[] resources,
                                    OverigeProductenDataPrepper overigeProductenDataPrepper,
                                    DatabaseLoader loader) {
        super(extendedLoggerFactory, resources, overigeProductenDataPrepper, loader);
        categories = new HashMap<>();
        products = new HashMap<>();
        prices = new HashMap<>();
        category_parent = new HashMap<>();
        price_product = new HashMap<>();
        product_category = new HashMap<>();
    }

    @Override
    protected void doImport(List<OverigProductCsvLine> csvLines) {
        super.doImport(csvLines); // calls transformAndLoad below
        loadCachedRelationships();
    }

    @Override
    protected void transformAndLoad(OverigProductCsvLine l) {
        final double TAXRATE = 0.06; // TODO: user input?
        final boolean ISAVAILABLE = true;
        final Date FROMDATE = new Date();

        Product product = new Product(null, l.getProductName(), l.getProductDescription(),
                l.isSpicy(), l.isVegetarian(), ISAVAILABLE, TAXRATE, null);
        Category mainCat = new Category(null, null, l.getCategoryName());
        Category subCat = new Category(null, null, l.getSubCategoryName());
        ProductPrice price = new ProductPrice(null, product, l.getPrice(), FROMDATE);

        if (!categories.containsKey(mainCat.getCatid())) {
            mainCat = loader.toDb(mainCat);
            categories.put(mainCat.getCatid(), mainCat);
        }

        if (!categories.containsKey(subCat.getCatid())) {
            subCat = loader.toDb(subCat);
            categories.put(subCat.getCatid(), subCat);
            category_parent.put(subCat, categories.get(mainCat.getCatid()));
        }

        if (!products.containsKey(product.getProductid())) {
            product = loader.toDb(product);
            products.put(product.getProductid(), product);
            product_category.put(product, categories.get(mainCat.getCatid()));
            product_category.put(product, categories.get(subCat.getCatid()));
        }

        if (!prices.containsKey(price.getPriceid())) {
            price = loader.toDb(price, product);
            prices.put(price.getPriceid(), price);
            price_product.put(price, products.get(product.getProductid()));
        }
    }

    protected void loadCachedRelationships() {
        for (Map.Entry<Category, Category> entry : category_parent.entrySet()) {
            entry.getKey().setParent(entry.getValue());
        }
        for (Map.Entry<ProductPrice, Product> entry : price_product.entrySet()) {
            entry.getKey().setProduct(entry.getValue());
        }
        for (Map.Entry<Product, Category> entry : product_category.entrySet()) {
            entry.getKey().addCategory(entry.getValue());
        }
    }
}
