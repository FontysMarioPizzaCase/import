package me.fontys.semester4.dominos;

import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.Store;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.data.repository.StoreRepository;
import me.fontys.semester4.dominos.pizza.and.ingredient.PizzaAndIngredientConverter;
import me.fontys.semester4.dominos.pizza.and.ingredient.PizzaAndIngredientImporter;
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
public class StoreImport implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StoreImport.class, args);
    }

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final PizzaAndIngredientConverter pizzaAndIngredientConverter;
    private final PizzaAndIngredientImporter pizzaAndIngredientImporter;

    @Autowired
    public StoreImport(StoreRepository storeRepository,
                       ProductRepository productRepository,
                       PizzaAndIngredientConverter pizzaAndIngredientConverter,
                       PizzaAndIngredientImporter pizzaAndIngredientImporter)
    {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.pizzaAndIngredientConverter = pizzaAndIngredientConverter;
        this.pizzaAndIngredientImporter = pizzaAndIngredientImporter;
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

        // import pizza_ingredients.csv
        PizzaAndIngredientConverter c = this.pizzaAndIngredientConverter;
        PizzaAndIngredientImporter i = this.pizzaAndIngredientImporter;

        try {
            // TODO: file not found
            String fileName = "dominos/src/main/resources/pizza_ingredienten.csv";
            char separator = ';';
            c.convert(fileName, separator);
            i.importProducts(c.getProducts());
            i.importCategories(c.getCategories());
            i.importIngredients(c.getIngredients());
            i.importProductPrices(c.getPrices());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
