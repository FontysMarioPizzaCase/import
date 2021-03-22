package me.fontys.semester4.dominos.configuration.data.catalog.datacleaners;

import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.OtherProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OtherProductsDataCleaner extends DataCleaner<OtherProductRawCsvLine, OtherProductCsvLine> {

    public OtherProductsDataCleaner(ExtendedLoggerFactory extendedLoggerFactory, CleanerUtil util) {
        super(extendedLoggerFactory, util);
    }

    @Override
    protected OtherProductCsvLine clean(OtherProductRawCsvLine raw) {
        String categoryName = util.cleanString(raw.getCategoryName());
        String subCategoryName = util.cleanString(raw.getSubCategoryName());
        String productName = util.cleanString(raw.getProductName());
        String productDescription = util.cleanString(raw.getProductDescription());
        BigDecimal price = util.cleanPrice(raw.getPrice());
        boolean isSpicy = util.cleanBool(raw.getIsSpicy(), "JA");
        boolean isVegetarian = util.cleanBool(raw.getIsVegetarian(), "JA");

        return new OtherProductCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, isSpicy, isVegetarian);
    }
}
