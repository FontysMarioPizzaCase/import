package me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients;

import me.fontys.semester4.dominos.configuration.data.catalog.general.DataValidator;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PizzaIngredientsDataValidator extends DataValidator<PizzaIngredientsRawCsvLine> {

    public PizzaIngredientsDataValidator(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory);
    }

    // TODO: refactor:
    //  - consolidate PizzaIngredient with OtherProducts (same-ish)
    //  - consolidate ExtraIngredient with Crust (same-ish)

    @Override
    protected void validate(PizzaIngredientsRawCsvLine line){
        if (line.getIngredientName().isEmpty()) {
            extendedLogger.processWarning("Record does not have an ingredient name");
            throw new IllegalArgumentException();
        }
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
        if (line.getDeliveryFee().isEmpty()) {
            extendedLogger.processWarning("Record does not have a product delivery fee");
        }
    }
}
