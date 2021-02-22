package me.fontys.semester4.dominos.configuration.data.catalog.prepare.extract;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PizzaIngredientsDataExtractor extends DataExtractor<PizzaIngredientsRawCsvLine> {

    public PizzaIngredientsDataExtractor(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory, PizzaIngredientsRawCsvLine.class);
    }
}
