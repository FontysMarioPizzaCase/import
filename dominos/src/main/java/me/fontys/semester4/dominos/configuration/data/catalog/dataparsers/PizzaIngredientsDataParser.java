package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util.ValidatorUtilFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PizzaIngredientsDataParser extends DataParser<PizzaIngredientsRawCsvLine, PizzaIngredientsCsvLine> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public PizzaIngredientsDataParser(DatabaseLoggerFactory databaseLoggerFactory, CleanerUtil cleanerUtil,
                                      ValidatorUtilFactory validatorUtilFactory) {
        super(databaseLoggerFactory, cleanerUtil, validatorUtilFactory);
    }

    @Override
    protected PizzaIngredientsCsvLine clean(PizzaIngredientsRawCsvLine raw) {
        String categoryName = cleanerUtil.cleanString(raw.getCategoryName());
        String subCategoryName = cleanerUtil.cleanString(raw.getSubCategoryName());
        String productName = cleanerUtil.cleanString(raw.getProductName());
        String productDescription = cleanerUtil.cleanString(raw.getProductDescription());
        BigDecimal price = cleanerUtil.cleanPrice(raw.getPrice());
        BigDecimal deliveryFee = cleanerUtil.cleanPrice(raw.getDeliveryFee());
        boolean isSpicy = cleanerUtil.cleanBool(raw.getIsSpicy(), "JA");
        boolean isVegetarian = cleanerUtil.cleanBool(raw.getIsVegetarian(), "JA");
        boolean isAvailable = cleanerUtil.cleanBool(raw.getIsAvailable(), "JA");
        int ingredientPortion = Integer.parseInt(raw.getIngredientPortion());
        String ingredientName = cleanerUtil.cleanString(raw.getIngredientName());
        String standardPizzasauce = cleanerUtil.cleanString(raw.getStandardPizzasauce());

        return new PizzaIngredientsCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, deliveryFee, isSpicy, isVegetarian, isAvailable,
                ingredientPortion, ingredientName, standardPizzasauce);
    }

    @Override
    protected void validate(PizzaIngredientsRawCsvLine line) {
        validatorUtil.validateNotEmpty(line.getIngredientName(), "ingredient name", Severity.ERROR);
        validatorUtil.validateNotEmpty(line.getDeliveryFee(), "product delivery fee", Severity.ERROR);
        validatorUtil.validatePriceFormat(line.getDeliveryFee(), "product delivery fee", Severity.WARN);
        validatorUtil.validateProductData(line);
    }
}
