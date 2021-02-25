package me.fontys.semester4.dominos;

import me.fontys.semester4.data.entity.Store;
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

    @Autowired
    public DominosImport(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void run(String... args) {

        // Example code
        this.storeRepository.save(new Store("Dominos Sittard", "Stationsstraat 49"));
        this.storeRepository.save(new Store("Dominos Eindhoven", "Karel de Grotelaan 353A"));
    }
}
