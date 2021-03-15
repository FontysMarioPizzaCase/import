package me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataCleaner;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CrustsDataCleaner extends DataCleaner<CrustRawCsvLine, CrustCsvLine> {

    public CrustsDataCleaner(ExtendedLoggerFactory extendedLoggerFactory, CleanerUtil util) {
        super(extendedLoggerFactory, util);
    }

    @Override
    protected CrustCsvLine clean(CrustRawCsvLine raw) {
        String crustName = util.cleanString(raw.getCrustName());
        String description = util.cleanString(raw.getDescription());
        int size = util.cleanInteger(raw.getSize());
        BigDecimal addPrice = util.cleanPrice(raw.getAddPrice());
        boolean isAvailable = util.cleanBool(raw.getIsAvailable(), "JA");

        return new CrustCsvLine(crustName, description, size, addPrice, isAvailable);
    }
}
