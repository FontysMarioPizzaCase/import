package me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PizzaIngredientsDataExtractor extends DataExtractor<PizzaIngredientsRawCsvLine> {

    public PizzaIngredientsDataExtractor(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory, PizzaIngredientsRawCsvLine.class);
    }
}
