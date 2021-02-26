package me.fontys.semester4.dominos;

import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.Store;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.data.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "me.fontys.semester4")
@EnableJpaRepositories(basePackages = "me.fontys.semester4")
public class DominosImport implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DominosImport.class, args);
    }

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DominosImport(StoreRepository storeRepository, ProductRepository productRepository)
    {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {

        // Example code
        this.storeRepository.save(new Store(1L, "Dominos Sittard", "Stationsstraat 49"));
        this.storeRepository.save(new Store(2L, "Dominos Eindhoven", "Karel de Grotelaan 353A"));
        this.productRepository.save(new Product(1L, "Pizza Margarita", 0.06, null ));
    }
}
