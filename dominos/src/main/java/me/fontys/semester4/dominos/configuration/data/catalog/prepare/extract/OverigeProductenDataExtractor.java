package me.fontys.semester4.dominos.configuration.data.catalog.prepare.extract;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.OverigProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OverigeProductenDataExtractor extends DataExtractor<OverigProductRawCsvLine> {

    public OverigeProductenDataExtractor(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory, OverigProductRawCsvLine.class);
    }
}
