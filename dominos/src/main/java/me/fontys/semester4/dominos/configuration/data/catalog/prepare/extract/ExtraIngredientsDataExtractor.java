package me.fontys.semester4.dominos.configuration.data.catalog.prepare.extract;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExtraIngredientsDataExtractor extends DataExtractor<ExtraIngredientRawCsvLine> {

    public ExtraIngredientsDataExtractor(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory, ExtraIngredientRawCsvLine.class);
    }
}
