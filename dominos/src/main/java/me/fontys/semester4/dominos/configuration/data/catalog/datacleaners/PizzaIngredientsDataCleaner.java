package me.fontys.semester4.dominos.configuration.data.catalog.datacleaners;

import me.fontys.semester4.dominos.configuration.data.catalog.models.cleaned_csv_models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PizzaIngredientsDataCleaner extends DataCleaner<PizzaIngredientsRawCsvLine, PizzaIngredientsCsvLine> {

    public PizzaIngredientsDataCleaner(DatabaseLoggerFactory databaseLoggerFactory,
                                       CleanerUtil util) {
        super(databaseLoggerFactory, util);
    }

    @Override
    protected PizzaIngredientsCsvLine clean(PizzaIngredientsRawCsvLine raw) {
        String categoryName = util.cleanString(raw.getCategoryName());
        String subCategoryName = util.cleanString(raw.getSubCategoryName());
        String productName = util.cleanString(raw.getProductName());
        String productDescription = util.cleanString(raw.getProductDescription());
        BigDecimal price = util.cleanPrice(raw.getPrice());
        BigDecimal deliveryFee = util.cleanPrice(raw.getDeliveryFee());
        boolean isSpicy = util.cleanBool(raw.getIsSpicy(), "JA");
        boolean isVegetarian = util.cleanBool(raw.getIsVegetarian(), "JA");
        boolean isAvailable = util.cleanBool(raw.getIsAvailable(), "JA");
        int ingredientPortion = Integer.parseInt(raw.getIngredientPortion());
        String ingredientName = util.cleanString(raw.getIngredientName());
        String standardPizzasauce = util.cleanString(raw.getStandardPizzasauce());

        return new PizzaIngredientsCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, deliveryFee, isSpicy, isVegetarian, isAvailable,
                ingredientPortion, ingredientName, standardPizzasauce);
    }
}
