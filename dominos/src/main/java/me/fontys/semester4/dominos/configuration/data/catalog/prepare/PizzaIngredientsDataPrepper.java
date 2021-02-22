package me.fontys.semester4.dominos.configuration.data.catalog.prepare;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.clean.PizzaIngredientsDataCleaner;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.extract.PizzaIngredientsDataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.validate.PizzaIngredientsDataValidator;
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
