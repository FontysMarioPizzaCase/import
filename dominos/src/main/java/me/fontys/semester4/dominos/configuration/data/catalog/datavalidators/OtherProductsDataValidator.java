package me.fontys.semester4.dominos.configuration.data.catalog.datavalidators;

import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtherProductsDataValidator extends DataValidator<OtherProductRawCsvLine> {

    public OtherProductsDataValidator(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected void validate(OtherProductRawCsvLine line) {
        validateProductData(line);
    }
}
