package me.fontys.semester4.dominos.configuration.data.catalog.general;

import me.fontys.semester4.dominos.configuration.data.catalog.extra_ingredients.csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.overige_producten.csv_models.OverigProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.pizza_ingredients.csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataExtractorFactory {
    private final DataExtractor<ExtraIngredientRawCsvLine> extraIngredientsDataExtractor;
    private final DataExtractor<OverigProductRawCsvLine> overigeProductenDataExtractor;
    private final DataExtractor<PizzaIngredientsRawCsvLine> pizzaIngredientsDataExtractor;
    private final DataExtractor<CrustRawCsvLine> crustsDataExtractor;

    public DataExtractorFactory(ExtendedLoggerFactory extendedLoggerFactory){
        // create new extractors
        this.extraIngredientsDataExtractor = new DataExtractor<>(extendedLoggerFactory,
                ExtraIngredientRawCsvLine.class);
        this.overigeProductenDataExtractor = new DataExtractor<>(extendedLoggerFactory,
                OverigProductRawCsvLine.class);
        this.pizzaIngredientsDataExtractor = new DataExtractor<>(extendedLoggerFactory,
                PizzaIngredientsRawCsvLine.class);
        this.crustsDataExtractor = new DataExtractor<>(extendedLoggerFactory,
                CrustRawCsvLine.class);
    }

    public DataExtractor<OverigProductRawCsvLine> getOverigeProductenDataExtractor() {
        return overigeProductenDataExtractor;
    }

    public DataExtractor<ExtraIngredientRawCsvLine> getExtraIngredientsDataExtractor() {
        return extraIngredientsDataExtractor;
    }

    public DataExtractor<PizzaIngredientsRawCsvLine> getPizzaIngredientsDataExtractor() {
        return pizzaIngredientsDataExtractor;
    }

    public DataExtractor<CrustRawCsvLine> getCrustsDataExtractor() {
        return crustsDataExtractor;
    }
}
