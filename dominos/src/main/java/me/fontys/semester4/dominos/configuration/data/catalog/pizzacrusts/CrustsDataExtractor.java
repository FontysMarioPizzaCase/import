package me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrustsDataExtractor extends DataExtractor<CrustRawCsvLine> {

    public CrustsDataExtractor(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory, CrustRawCsvLine.class);
    }
}
