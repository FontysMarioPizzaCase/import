package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.ValidatorUtilFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.OtherProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OtherProductsDataParser extends DataParser<OtherProductRawCsvLine, OtherProductCsvLine> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public OtherProductsDataParser(DatabaseLoggerFactory databaseLoggerFactory,
                                   CleanerUtil cleanerUtil, ValidatorUtilFactory validatorUtilFactory) {
        super(databaseLoggerFactory, cleanerUtil, validatorUtilFactory);
    }

    @Override
    protected OtherProductCsvLine clean(OtherProductRawCsvLine raw) {
        String categoryName = cleanerUtil.cleanString(raw.getCategoryName());
        String subCategoryName = cleanerUtil.cleanString(raw.getSubCategoryName());
        String productName = cleanerUtil.cleanString(raw.getProductName());
        String productDescription = cleanerUtil.cleanString(raw.getProductDescription());
        BigDecimal price = cleanerUtil.cleanPrice(raw.getPrice());
        boolean isSpicy = cleanerUtil.cleanBool(raw.getIsSpicy(), "JA");
        boolean isVegetarian = cleanerUtil.cleanBool(raw.getIsVegetarian(), "JA");

        return new OtherProductCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, isSpicy, isVegetarian);
    }

    @Override
    protected void validate(OtherProductRawCsvLine line) {
        validatorUtil.validateProductData(line);
    }
}
