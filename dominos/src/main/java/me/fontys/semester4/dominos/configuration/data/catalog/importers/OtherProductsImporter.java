package me.fontys.semester4.dominos.configuration.data.catalog.importers;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper.*;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.parsers.OtherProductsParser;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.extractors.ExtractorFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.other.Relationship;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
public class OtherProductsImporter extends CsvImporter<OtherProductRawCsvLine, ProductCsvLine> {
    private final Map<Long, Category> categories;
    private final Map<Long, Product> products;
    private final Map<Long, ProductPrice> prices;

    private final Set<Relationship<Category, Category>> category_parent;
    private final Set<Relationship<ProductPrice, Product>> price_product;
    private final Set<Relationship<Product, Category>> product_category;

    private final CategoryRepoHelper categoryRepoHelper;
    private final ProductRepoHelper productRepoHelper;
    private final ProductPriceRepoHelper priceRepoHelper;

    final boolean ISAVAILABLE;
    final double TAXRATE;
    final Date FROMDATE;

    @Autowired
    public OtherProductsImporter(Environment environment,
                                 DatabaseLoggerFactory databaseLoggerFactory,
                                 @Qualifier("overigeProducten") Resource[] resources,
                                 ExtractorFactory extractorFactory,
                                 OtherProductsParser cleaner,
                                 RepoHelperFactory repoHelperFactory) {
        super(environment, databaseLoggerFactory,
                resources, extractorFactory.getOverigeProductenDataExtractor(),
                cleaner);
        categories = new HashMap<>();
        products = new HashMap<>();
        prices = new HashMap<>();
        category_parent = new HashSet<>();
        price_product = new HashSet<>();
        product_category = new HashSet<>();
        this.categoryRepoHelper = repoHelperFactory.getCategoryRepoHelper(log);
        this.productRepoHelper = repoHelperFactory.getProductRepoHelper(log);
        this.priceRepoHelper = repoHelperFactory.getProductPriceRepoHelper(log);

        // init constants
        TAXRATE = Double.parseDouble(Objects.requireNonNull(environment.getProperty(
                "catalog.pizzaingredientsimport.default_taxrate_for_products")));
        ISAVAILABLE = Boolean.parseBoolean(environment.getProperty(
                "catalog.pizzaingredientsimport.default_isavailable_for_products"));

        Date tempDate;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            tempDate = dateFormatter.parse(environment.getProperty(
                    "catalog.pizzaingredientsimport.default_fromdate_for_price"));
        } catch (ParseException e) {
            tempDate = new Date();
        }
        FROMDATE = tempDate;
    }

    @Override
    protected void transformAndLoad(ProductCsvLine l) {
        Product product = new Product(null, l.getProductName(), l.getProductDescription(),
                l.isSpicy(), l.isVegetarian(), ISAVAILABLE, TAXRATE, null);
        Category mainCat = new Category(null, null, l.getCategoryName());
        Category subCat = new Category(null, null, l.getSubCategoryName());
        ProductPrice price = new ProductPrice(null, product, l.getPrice(), FROMDATE);

        // cache main category
        mainCat = categoryRepoHelper.saveOrUpdate(mainCat);
        categories.put(mainCat.getCatid(), mainCat);

        // cache subcategory and relationships to parent
        subCat = categoryRepoHelper.saveOrUpdate(subCat);
        categories.put(subCat.getCatid(), subCat);
        category_parent.add(new Relationship<>(subCat, categories.get(mainCat.getCatid())));

        // cache product and relationship to categories
        product = productRepoHelper.saveOrUpdate(product);
        products.put(product.getProductid(), product);
        product_category.add(new Relationship<>(product, categories.get(mainCat.getCatid())));
        product_category.add(new Relationship<>(product, categories.get(subCat.getCatid())));

        // cache ingredient and relationship to product
        price = priceRepoHelper.saveOrUpdate(price, product);
        prices.put(price.getPriceid(), price);
        price_product.add(new Relationship<>(price, products.get(product.getProductid())));
    }

    @Override
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
