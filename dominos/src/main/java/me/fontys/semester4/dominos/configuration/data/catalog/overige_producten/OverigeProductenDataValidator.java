package me.fontys.semester4.dominos.configuration.data.catalog.overige_producten;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataValidator;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OverigeProductenDataValidator extends DataValidator<OverigProductRawCsvLine> {

    public OverigeProductenDataValidator(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory);
    }

    @Override
    protected void validate(OverigProductRawCsvLine line) {
        if (line.getCategoryName().isEmpty()) {
            extendedLogger.processWarning("Record does not have a category name");
            throw new IllegalArgumentException();
        }
        if (line.getSubCategoryName().isEmpty()) {
            extendedLogger.processWarning("Record does not have a subcategory name");
            throw new IllegalArgumentException();
        }
        if (line.getProductName().isEmpty()) {
            extendedLogger.processWarning("Record does not have a product name");
            throw new IllegalArgumentException();
        }
        if (line.getPrice().isEmpty()) {
            extendedLogger.processWarning("Record does not have a product price");
            throw new IllegalArgumentException();
        }
        if (line.getProductDescription().isEmpty()) {
            extendedLogger.processWarning("Record does not have a product description");
        }
        if (line.getIsSpicy().isEmpty()) {
            extendedLogger.processWarning("Record does not have a product spicy indicator");
        }
        if (line.getIsVegetarian().isEmpty()) {
            extendedLogger.processWarning("Record does not have a product vegetarian indicator");
        }
    }
}
