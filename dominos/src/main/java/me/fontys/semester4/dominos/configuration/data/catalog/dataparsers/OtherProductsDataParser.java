package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.OtherProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OtherProductsDataParser extends DataParser<OtherProductRawCsvLine, OtherProductCsvLine> {

    public OtherProductsDataParser(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected OtherProductCsvLine parse(OtherProductRawCsvLine raw) {
        String categoryName = parseString(raw.getCategoryName(), "category name", Severity.ERROR);
        String subCategoryName = parseString(raw.getSubCategoryName(), "subcategory name", Severity.ERROR);
        String productName = parseString(raw.getProductName(), "product name", Severity.ERROR);
        String productDescription = parseString(raw.getProductDescription(),
                "product description", Severity.WARN);
        BigDecimal price = parsePrice(raw.getPrice(), "product price", Severity.ERROR);
        boolean isSpicy = parseBoolean(raw.getIsSpicy(), "JA",
                "product spicy indicator", Severity.WARN);
        boolean isVegetarian = parseBoolean(raw.getIsVegetarian(), "JA",
                "product vegetarian indicator", Severity.WARN);

        return new OtherProductCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, isSpicy, isVegetarian);
    }
}
