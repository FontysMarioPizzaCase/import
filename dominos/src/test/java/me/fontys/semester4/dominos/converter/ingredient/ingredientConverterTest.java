package me.fontys.semester4.dominos.converter.ingredient;

import org.junit.Test;
import java.io.FileNotFoundException;

public class ingredientConverterTest {

    @Test
    public void Convert_works() {
        // ARRANGE
        String fileName = "C:\\Users\\884573\\Downloads\\MarioData\\pizza_ingredienten.csv";
        char seperator = ';';
        var ingredientConverter = new PizzaAndIngredientConverter();

        // ACT
        try {
            ingredientConverter.Analyse(fileName, seperator);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}