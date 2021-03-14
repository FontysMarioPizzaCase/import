package me.fontys.semester4.dominos.configuration.data.catalog.prepare.clean;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.PizzaIngredientsCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.prepare.models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PizzaIngredientsDataCleaner extends DataCleaner<PizzaIngredientsRawCsvLine, PizzaIngredientsCsvLine> {

    public PizzaIngredientsDataCleaner(ExtendedLoggerFactory extendedLoggerFactory,
                                       CleanerUtil util) {
        super(extendedLoggerFactory, util);
    }

    @Override
    protected PizzaIngredientsCsvLine clean(PizzaIngredientsRawCsvLine raw) {
        String categoryName = util.cleanString(raw.getCategoryName());
        String subCategoryName = util.cleanString(raw.getSubCategoryName());
        String productName = util.cleanString(raw.getProductName());
        String productDescription = util.cleanString(raw.getProductDescription());
        BigDecimal price = util.cleanPrice(raw.getPrice());
        BigDecimal deliveryFee = util.cleanPrice(raw.getDeliveryFee());
        boolean isSpicy = raw.getIsSpicy().equalsIgnoreCase("JA");
        boolean isVegetarian = raw.getIsVegetarian().equalsIgnoreCase("JA");
        boolean isAvailable = raw.getIsVegetarian().equalsIgnoreCase("JA");
        int ingredientPortion = Integer.parseInt(raw.getIngredientPortion());
        String ingredientName = util.cleanString(raw.getIngredientName());
        String standardPizzasauce = util.cleanString(raw.getStandardPizzasauce());

        return new PizzaIngredientsCsvLine(categoryName, subCategoryName, productName,
                productDescription, price, deliveryFee, isSpicy, isVegetarian, isAvailable,
                ingredientPortion, ingredientName, standardPizzasauce);
    }
}
