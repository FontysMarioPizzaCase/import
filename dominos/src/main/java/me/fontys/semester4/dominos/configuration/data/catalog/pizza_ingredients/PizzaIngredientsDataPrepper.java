package me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataPrepper;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PizzaIngredientsDataPrepper extends DataPrepper<PizzaIngredientsRawCsvLine, PizzaIngredientsCsvLine> {

    @Autowired
    public PizzaIngredientsDataPrepper(ExtendedLoggerFactory extendedLoggerFactory,
                                       PizzaIngredientsDataExtractor dataExtractor,
                                       PizzaIngredientsDataValidator validator,
                                       PizzaIngredientsDataCleaner cleaner) {
        super(extendedLoggerFactory, dataExtractor, validator, cleaner);
    }
}
