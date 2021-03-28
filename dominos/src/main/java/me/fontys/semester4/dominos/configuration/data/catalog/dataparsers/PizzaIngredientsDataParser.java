package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.ProductCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.PizzaIngredientsRawCsvLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PizzaIngredientsDataParser extends DataParser<PizzaIngredientsRawCsvLine, PizzaIngredientsCsvLine> {

    public PizzaIngredientsDataParser(DatabaseLoggerFactory databaseLoggerFactory) {
        super(databaseLoggerFactory);
    }

    @Override
    protected PizzaIngredientsCsvLine parse(PizzaIngredientsRawCsvLine raw) {
        ProductCsvLine productData = parseProduct(raw);

        int ingredientPortion = parseInteger(raw.getIngredientPortion(),
                "ingredient portion", Severity.INFO);
        String standardPizzasauce = parseString(raw.getStandardPizzasauce(),
                "standard pizza sauce", Severity.INFO);
        BigDecimal deliveryFee = parsePrice(raw.getDeliveryFee(), "product delivery fee", Severity.ERROR);
        String ingredientName = parseString(raw.getIngredientName(), "ingredient name", Severity.ERROR);
        boolean isAvailable = parseBoolean(raw.getIsAvailable(),
                "availability", Severity.WARN);

        return new PizzaIngredientsCsvLine(productData, deliveryFee, isAvailable,
                ingredientPortion, ingredientName, standardPizzasauce);
    }
}
