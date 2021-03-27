package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.PizzaIngredientsRawCsvLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PizzaIngredientsDataParser extends DataParser<PizzaIngredientsRawCsvLine, PizzaIngredientsCsvLine> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public PizzaIngredientsDataParser(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected PizzaIngredientsCsvLine parse(PizzaIngredientsRawCsvLine raw) {
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

        int ingredientPortion = parseInteger(raw.getIngredientPortion(),
                "ingredient portion", Severity.INFO);
        String standardPizzasauce = parseString(raw.getStandardPizzasauce(),
                "standard pizza sauce", Severity.INFO);
        BigDecimal deliveryFee = parsePrice(raw.getDeliveryFee(), "product delivery fee", Severity.ERROR);
        String ingredientName = parseString(raw.getIngredientName(), "ingredient name", Severity.ERROR);
        boolean isAvailable = parseBoolean(raw.getIsAvailable(), "JA",
                "availability", Severity.WARN);

        return new PizzaIngredientsCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, deliveryFee, isSpicy, isVegetarian, isAvailable,
                ingredientPortion, ingredientName, standardPizzasauce);
    }
}
