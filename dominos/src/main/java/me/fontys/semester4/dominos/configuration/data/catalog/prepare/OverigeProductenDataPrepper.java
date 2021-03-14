package me.fontys.semester4.dominos.configuration.data.catalog.prepare;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.clean.ExtraIngredientDataCleaner;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.clean.OverigeProductenDataCleaner;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.extract.ExtraIngredientsDataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.extract.OverigeProductenDataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.ExtraIngredientCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.OverigProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.OverigProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.validate.ExtraIngredientDataValidator;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.validate.OverigeProductenDataValidator;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OverigeProductenDataPrepper extends DataPrepper<OverigProductRawCsvLine, OverigProductCsvLine> {

    @Autowired
    public OverigeProductenDataPrepper(ExtendedLoggerFactory extendedLoggerFactory,
                                       OverigeProductenDataExtractor dataExtractor,
                                       OverigeProductenDataValidator validator,
                                       OverigeProductenDataCleaner cleaner) {
        super(extendedLoggerFactory, dataExtractor, validator, cleaner);
    }
}
