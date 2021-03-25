package me.fontys.semester4.dominos.configuration.data.catalog.dataloader;

import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoaderFactory {
    private final DatabaseLoggerFactory databaseLoggerFactory;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductPriceRepository productPriceRepository;

    @Autowired
    public DatabaseLoaderFactory(DatabaseLoggerFactory databaseLoggerFactory,
                                 ProductRepository productRepository, CategoryRepository categoryRepository,
                                 IngredientRepository ingredientRepository,
                                 ProductPriceRepository productPriceRepository){
        this.databaseLoggerFactory = databaseLoggerFactory;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.productPriceRepository = productPriceRepository;
    }

    public DatabaseLoader getDatabaseLoader(){
        return new DatabaseLoader(databaseLoggerFactory, productRepository, categoryRepository, ingredientRepository,
                productPriceRepository);
    }
}
