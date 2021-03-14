package me.fontys.semester4.dominos.configuration.data.catalog.overige_producten;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OverigeProductenDataExtractor extends DataExtractor<OverigProductRawCsvLine> {

    public OverigeProductenDataExtractor(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory, OverigProductRawCsvLine.class);
    }
}
