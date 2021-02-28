package me.fontys.semester4.dominos.converter.ingredient;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class IngredientConverter {

    public void Convert(String fileName) throws FileNotFoundException {

        List<IngredientRecord> records = new CsvToBeanBuilder(new FileReader(fileName))
                .withSeparator(';')
                .withType(IngredientRecord.class)
                .build()
                .parse();

        // check in console
        records.forEach(System.out::println);
    }
}
