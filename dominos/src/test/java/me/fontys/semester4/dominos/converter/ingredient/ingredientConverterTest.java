package me.fontys.semester4.dominos.converter.ingredient;

import org.junit.Test;
import java.io.FileNotFoundException;

public class ingredientConverterTest {

    @Test
    public void Convert_works() {
        // ARRANGE
        String fileName = "C:\\Users\\884573\\Downloads\\MarioData\\pizza_ingredienten.csv";
        var ingredientConverter = new IngredientConverter();
        
        // ACT
        try {
            ingredientConverter.Convert(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}