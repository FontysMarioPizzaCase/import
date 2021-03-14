package me.fontys.semester4.dominos.configuration.data.catalog.prepare.clean;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.*;
import me.fontys.semester4.dominos.configuration.data.catalog.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OverigeProductenDataCleaner extends DataCleaner<OverigProductRawCsvLine, OverigProductCsvLine> {

    public OverigeProductenDataCleaner(ExtendedLoggerFactory extendedLoggerFactory, CleanerUtil util) {
        super(extendedLoggerFactory, util);
    }

    @Override
    protected OverigProductCsvLine clean(OverigProductRawCsvLine raw) {
        String categoryName = util.cleanString(raw.getCategoryName());
        String subCategoryName = util.cleanString(raw.getSubCategoryName());
        String productName = util.cleanString(raw.getProductName());
        String productDescription = util.cleanString(raw.getProductDescription());
        BigDecimal price = util.cleanPrice(raw.getPrice());
        boolean isSpicy = util.cleanBool(raw.getIsSpicy(), "JA");
        boolean isVegetarian = util.cleanBool(raw.getIsVegetarian(), "JA");

        return new OverigProductCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, isSpicy, isVegetarian);
    }
}
