package me.fontys.semester4.dominos.pizza.and.ingredient;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.entity.ProductPrice;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class PizzaAndIngredientConverterTest {
    private String fileName = "C:\\Users\\884573\\Documents\\Repositories\\_Semester_4_ImportApp\\dominos\\src\\test\\resources\\pizza_ingredienten_test.csv";
    private char separator = ';';

    @Test
    public void Convert_works() {
        // TODO: Test all entity properties?

        // ARRANGE
        var c = new PizzaAndIngredientConverter();
        List<Product> products = c.getProducts();
        List<Category> categories = c.getCategories();
        List<Ingredient> ingredients = c.getIngredients();
        List<ProductPrice> prices = c.getPrices();

        // ACT
        try {
            c.convert(fileName, separator);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // ASSERT
        products.forEach(System.out::println);
        categories.forEach(System.out::println);
        ingredients.forEach(System.out::println);
        prices.forEach(System.out::println);
        assertFalse(c.getProducts().isEmpty());
        assertFalse(c.getCategories().isEmpty());
        assertFalse(c.getIngredients().isEmpty());
        assertFalse(c.getPrices().isEmpty());
    }
}