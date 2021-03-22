package me.fontys.semester4.dominos.configuration.data.catalog.datavalidators;

import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PizzaIngredientsDataValidator extends DataValidator<PizzaIngredientsRawCsvLine> {

    public PizzaIngredientsDataValidator(ExtendedLoggerFactory extendedLoggerFactory) {
        super(extendedLoggerFactory);
    }

    @Override
    protected void validate(PizzaIngredientsRawCsvLine line) {
        skipOnFail(validateNotEmpty(line.getIngredientName(), "ingredient name"));
        validatePriceFormat(line.getDeliveryFee(), "product delivery fee");
        validateProductData(line);
    }
}
