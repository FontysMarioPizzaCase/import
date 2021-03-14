package me.fontys.semester4.dominos.configuration.data.catalog.overige_producten;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataPrepper;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductRawCsvLine;
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
