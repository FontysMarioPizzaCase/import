package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OtherProductsDataParser extends DataParser<OtherProductRawCsvLine, ProductCsvLine> {

    public OtherProductsDataParser(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected ProductCsvLine parse(OtherProductRawCsvLine raw) {
        return parseProduct(raw);
    }
}
