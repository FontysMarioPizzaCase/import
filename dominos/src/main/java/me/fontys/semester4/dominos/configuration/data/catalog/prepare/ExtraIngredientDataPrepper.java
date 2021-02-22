package me.fontys.semester4.dominos.configuration.data.catalog.prepare;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.clean.ExtraIngredientDataCleaner;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.extract.ExtraIngredientsDataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.validate.ExtraIngredientDataValidator;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtraIngredientDataPrepper extends DataPrepper<ExtraIngredientRawCsvLine, ExtraIngredientCsvLine> {

    @Autowired
    public ExtraIngredientDataPrepper(ExtendedLoggerFactory extendedLoggerFactory,
                                      ExtraIngredientsDataExtractor dataExtractor,
                                      ExtraIngredientDataValidator validator,
                                      ExtraIngredientDataCleaner cleaner) {
        super(extendedLoggerFactory, dataExtractor, validator, cleaner);
    }
}
