package me.fontys.semester4.dominos.converter.ingredient;

import me.fontys.semester4.data.repository.ProductRepository;

public class PizzaAndIngredientImporter {
    private final ProductRepository productRepository;

    public PizzaAndIngredientImporter(ProductRepository productRepository){

        this.productRepository = productRepository;

    }
}
