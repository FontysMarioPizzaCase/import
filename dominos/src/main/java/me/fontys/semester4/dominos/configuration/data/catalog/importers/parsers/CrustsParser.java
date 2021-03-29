package me.fontys.semester4.dominos.configuration.data.catalog.importers.parsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.CrustRawCsvLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CrustsParser extends Parser<CrustRawCsvLine, CrustCsvLine> {

    public CrustsParser(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected CrustCsvLine parse(CrustRawCsvLine raw) {
        String crustName = parseString(raw.getCrustName(),"crust name", Severity.ERROR);
        String description = parseString(raw.getDescription(), "crust description", Severity.WARN);
        int size = parseInteger(raw.getSize(), "crust size", Severity.ERROR);
        BigDecimal addPrice = parsePrice(raw.getAddPrice(), "crust surcharge", Severity.ERROR);
        boolean isAvailable = parseBoolean(raw.getIsAvailable(), "availability indicator", Severity.WARN);

        return new CrustCsvLine(crustName, description, size, addPrice, isAvailable);
    }
}