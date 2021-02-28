package me.fontys.semester4.dominos.pizza.and.ingredient;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.data.repository.ProductPriceRepository;
import me.fontys.semester4.data.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaAndIngredientImporter {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPriceRepository productPriceRepository;

    public PizzaAndIngredientImporter(ProductRepository productRepository,
                                      IngredientRepository ingredientRepository,
                                      CategoryRepository categoryRepository,
                                      ProductPriceRepository productPriceRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
        this.productPriceRepository = productPriceRepository;
    }

    public void importProducts(List<Product> products){
        for (var product : products) {
            this.productRepository.save(product);
        }
    }
    public void importIngredients(List<Ingredient> ingredients){
        for (var ingredient : ingredients) {
            this.ingredientRepository.save(ingredient);
        }
    }
    public void importCategories(List<Category> categories){
        for (var category : categories) {
            this.categoryRepository.save(category);
        }
    }
    public void importProductPrices(List<ProductPrice> prices){
        for (var price : prices) {
            this.productPriceRepository.save(price);
        }
    }
}
