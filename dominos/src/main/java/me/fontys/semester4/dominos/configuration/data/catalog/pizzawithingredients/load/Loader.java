package me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.load;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.dominos.configuration.data.catalog.extraingredientsurcharge.ExtraIngredientSurchargeRecord;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.Importer;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.extract.RawCsvLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

@Service
public class Loader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Importer.class);
    private final Map<String, Integer> warnings = new HashMap<>();


    public void loadIntoDb(Set<Product> products, Set<Category> categories,
                           Set<Ingredient> ingredients, Set<ProductPrice> prices) {
        LOGGER.info(String.format("Loading into db: \n" +
                "- %s products \n" +
                "- %s categories \n" +
                "- %s ingredients \n" +
                "- %s product prices", products.size(), categories.size(),
                ingredients.size(), prices.size()));
        this.warnings.clear();

        // TODO: logic that checks exists, and then bulk inserts/updates??
    }


//    private Ingredient saveNewOrUpdate(RawCsvLine record) {
//        Ingredient ingredient;
//        Optional<Ingredient> temp = this.repository.findByName(record.getIngredientName());
//
//        if(temp.isPresent()){
//            ingredient = temp.get();
//            processWarning("No Ingredient properties to update");
//        }
//        else {
//            ingredient = saveNewIngredient(record);
//            processWarning("Ingredient created");
//        }
//        return ingredient;
//    }
//
//    private Ingredient saveNewIngredient(RawCsvLine record) {
//        Ingredient ingredient = new Ingredient(null, record.getIngredientName(), null);
//        this.repository.save(ingredient);
//        return ingredient;
//    }
//
//
//    private Category saveNewOrUpdate(String name) {
//        Category category;
//        Optional<Category> temp = this.repository.findByName(name);
//
//        if(temp.isPresent()){
//            category = temp.get();
//            processWarning("No Category properties to update");
//        }
//        else {
//            category = saveNewCategory(name);
//            processWarning("Category created");
//        }
//        return category;
//    }
//
//    private Category saveNewCategory(String name) {
//        Category category = new Category(null, null, name);
//        this.repository.save(category);
//        return category;
//    }
//
//    private ProductPrice saveNewOrUpdate(Product product, BigDecimal price) {
//        ProductPrice productPrice;
//        Optional<ProductPrice> temp;
//
//        try (Stream<ProductPrice> stream = this.repository
//                .findByPriceAndProduct_Productid(price.toString(), product.getProductid())) {
//            temp = stream.findFirst();
//        } catch (Exception e) {
//            processWarning(String.format("Could not query db for ProductPrice: %s", e.toString()));
//            throw e;
//        }
//
//        if (temp.isPresent()) {
//            productPrice = temp.get();
//            processWarning("No ProductPrice properties to update");
//        } else {
//            productPrice = saveNewProductPrice(price, product);
//            processWarning("ProductPrice created");
//        }
//
//        return productPrice;
//    }
//
//    private ProductPrice saveNewProductPrice(BigDecimal price, Product product) {
//        ProductPrice productPrice = new ProductPrice(
//                null,
//                product,
//                price,
//                new Date()
//        );
//        this.repository.save(productPrice);
//        return productPrice;
//    }
//
//
//
//    private Product saveNewOrUpdate(RawCsvLine record, Ingredient ingredient, List<Category> categories) {
//        Product product;
//        Optional<Product> temp = this.repository.findByName(record.getProductName()); // TODO: iets met case + unique constraint op naam
//
//        if(temp.isPresent()){
//            product = temp.get();
//            product = updateProduct(product, record);
//            processWarning("Product updated");
//        }
//        else {
//            product = saveNewProduct(record);
//            processWarning("Product created");
//        }
//
//        addRelationships(product, ingredient, categories);
//
//        return product;
//    }
//
//    private Product updateProduct(Product product, RawCsvLine record) {
//        product.setName(record.getProductName()); // TODO: case en data aanpassingen nog
//        product.setDescription(record.getProductDescription());
//        product.setSpicy(record.getIsSpicy().equalsIgnoreCase("JA"));
//        product.setVegetarian(record.getIsVegetarian().equalsIgnoreCase("JA"));
//        product.setDeliveryfee(cleaner.cleanPrice(record.getDeliveryFee()));
//
//        return product;
//    }
//
//    private Product saveNewProduct(RawCsvLine record) {
//        Product product = new Product(
//                null,
//                record.getProductName(),
//                record.getProductDescription(),
//                record.getIsSpicy().equalsIgnoreCase("JA"),
//                record.getIsVegetarian().equalsIgnoreCase("JA"),
//                cleaner.cleanPrice(record.getDeliveryFee()),
//                // TODO: input taxrate?
//                0.06,
//                null
//        );
//        this.repository.save(product);
//
//        return product;
//    }
//
//    private void addRelationships(Product product, Ingredient ingredient, List<Category> categories) {
//        product.getIngredients().add(ingredient);
//        product.getCategories().addAll(categories);
//    }






    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Catalog import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
