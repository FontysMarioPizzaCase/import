package me.fontys.semester4.dominos.configuration.data.catalog.overige_producten;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.general.*;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.general.helper_models.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.*;

@Configuration
public class OverigeProductenImporter extends CsvImporter<OverigProductRawCsvLine, OverigProductCsvLine> {
    private final Map<Long, Category> categories;
    private final Map<Long, Product> products;
    private final Map<Long, ProductPrice> prices;

    private final Set<Relationship<Category, Category>> category_parent;
    private final Set<Relationship<ProductPrice, Product>> price_product;
    private final Set<Relationship<Product, Category>> product_category;

    @Autowired
    public OverigeProductenImporter(ExtendedLoggerFactory extendedLoggerFactory,
                                    @Qualifier("overigeProducten") Resource[] resources,
                                    OverigeProductenDataExtractor dataExtractor,
                                    OverigeProductenDataValidator validator, OverigeProductenDataCleaner cleaner,
                                    DatabaseLoader loader) {
        super(extendedLoggerFactory, resources, dataExtractor, validator, cleaner, loader);
        categories = new HashMap<>();
        products = new HashMap<>();
        prices = new HashMap<>();
        category_parent = new HashSet<>();
        price_product = new HashSet<>();
        product_category = new HashSet<>();
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

        // cache main category
        mainCat = loader.toDb(mainCat);
        categories.put(mainCat.getCatid(), mainCat);

        // cache subcategory and relationships to parent
        subCat = loader.toDb(subCat);
        categories.put(subCat.getCatid(), subCat);
        category_parent.add(new Relationship<>(subCat, categories.get(mainCat.getCatid())));

        // cache product and relationship to categories
        product = loader.toDb(product);
        products.put(product.getProductid(), product);
        product_category.add(new Relationship<>(product, categories.get(mainCat.getCatid())));
        product_category.add(new Relationship<>(product, categories.get(subCat.getCatid())));

        // cache ingredient and relationship to product
        price = loader.toDb(price, product);
        prices.put(price.getPriceid(), price);
        price_product.add(new Relationship<>(price, products.get(product.getProductid())));
    }

    protected void loadCachedRelationships() {
        for (Relationship<Category, Category> r : category_parent) {
            r.getLeft().setParent(r.getRight());
        }
        for (Relationship<ProductPrice, Product> r : price_product) {
            r.getLeft().setProduct(r.getRight());
        }
        for (Relationship<Product, Category> r : product_category) {
            r.getLeft().addCategory(r.getRight());
        }
    }
}
