package me.fontys.semester4.dominos.configuration.data.catalog.importers.parsers;

import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import org.springframework.stereotype.Service;

@Service
public class OtherProductsParser extends Parser<OtherProductRawCsvLine, ProductCsvLine> {

    public OtherProductsParser(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected ProductCsvLine parse(OtherProductRawCsvLine raw) {
        return parseProduct(raw);
    }
}
