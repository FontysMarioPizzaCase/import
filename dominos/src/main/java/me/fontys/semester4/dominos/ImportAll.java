package me.fontys.semester4.dominos;

import me.fontys.semester4.data.entity.PostalcodePart;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.Store;
import me.fontys.semester4.data.repository.*;
import me.fontys.semester4.dominos.pizza.and.ingredient.PizzaAndIngredientConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

@SpringBootApplication
@EntityScan(basePackages = "me.fontys.semester4")
@EnableJpaRepositories(basePackages = "me.fontys.semester4")
public class ImportAll implements CommandLineRunner {

    private final StoreRepository storeRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductRepository productRepository;
    private final PostalcodePartRepository postalcodePartRepository;
    private final PizzaAndIngredientConverter pizzaAndIngredientConverter;

    public static void main(String[] args) {
        SpringApplication.run(ImportAll.class, args);
    }


    @Autowired
    public ImportAll(StoreRepository storeRepository,
                     ProductRepository productRepository,
                     IngredientRepository ingredientRepository,
                     CategoryRepository categoryRepository,
                     ProductPriceRepository productPriceRepository,
                     PostalcodePartRepository postalcodePartRepository,
                     PizzaAndIngredientConverter pizzaAndIngredientConverter) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
        this.productPriceRepository = productPriceRepository;
        this.postalcodePartRepository = postalcodePartRepository;
        this.pizzaAndIngredientConverter = pizzaAndIngredientConverter;
    }

    @Override
    public void run(String... args) {

        // Example code
        this.storeRepository.save(new Store(1L, "Dominos Sittard", "Stationsstraat 49"));
        this.storeRepository.save(new Store(2L, "Dominos Eindhoven", "Karel de Grotelaan 353A"));
        this.productRepository.save(new Product(
                3L,
                "Pizza Margharita",
                "Pizza description",
                false,
                true,
                BigDecimal.valueOf(2.00),
                0.06,
                null));
        this.postalcodePartRepository.save(new PostalcodePart(
                null,
                null,
                null,
                null,
                null));

        importPizzaAndIngredientsCsv();
    }

    private void importPizzaAndIngredientsCsv() {
        try {
            // select file
            String fileName = System.getenv("CSVFILE");
            char separator = ';';

            // alias
            PizzaAndIngredientConverter c = this.pizzaAndIngredientConverter;

            // convert and import
            c.convert(fileName, separator);
            this.productRepository.saveAll(c.getProducts());
            this.categoryRepository.saveAll(c.getCategories());
            this.ingredientRepository.saveAll(c.getIngredients());
            this.productPriceRepository.saveAll(c.getPrices());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
