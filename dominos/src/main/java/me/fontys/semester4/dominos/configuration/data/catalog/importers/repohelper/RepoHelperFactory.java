package me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper;

import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoHelperFactory {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductPriceRepository productPriceRepository;

    @Autowired
    public RepoHelperFactory(ProductRepository productRepository, CategoryRepository categoryRepository,
                             IngredientRepository ingredientRepository,
                             ProductPriceRepository productPriceRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.productPriceRepository = productPriceRepository;
    }

    public IngredientRepoHelper getIngredientRepoHelper(DatabaseLogger log){
        return new IngredientRepoHelper(log, ingredientRepository);
    }

    public CategoryRepoHelper getCategoryRepoHelper(DatabaseLogger log){
        return new CategoryRepoHelper(log, categoryRepository);
    }

    public ProductRepoHelper getProductRepoHelper(DatabaseLogger log){
        return new ProductRepoHelper(log, productRepository);
    }

    public ProductPriceRepoHelper getProductPriceRepoHelper(DatabaseLogger log){
        return new ProductPriceRepoHelper(log, productPriceRepository);
    }
}
