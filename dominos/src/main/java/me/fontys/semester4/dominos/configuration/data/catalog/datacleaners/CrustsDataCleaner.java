package me.fontys.semester4.dominos.configuration.data.catalog.datacleaners;

import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CrustsDataCleaner extends DataCleaner<CrustRawCsvLine, CrustCsvLine> {

    public CrustsDataCleaner(DatabaseLoggerFactory databaseLoggerFactory, CleanerUtil util) {
        super(databaseLoggerFactory, util);
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
