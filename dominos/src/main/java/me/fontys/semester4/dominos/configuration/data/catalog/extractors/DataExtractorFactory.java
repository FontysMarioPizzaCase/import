package me.fontys.semester4.dominos.configuration.data.catalog.extractors;

import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataExtractorFactory {
    private final DataExtractor<ExtraIngredientRawCsvLine> extraIngredientsDataExtractor;
    private final DataExtractor<OtherProductRawCsvLine> overigeProductenDataExtractor;
    private final DataExtractor<PizzaIngredientsRawCsvLine> pizzaIngredientsDataExtractor;
    private final DataExtractor<CrustRawCsvLine> crustsDataExtractor;

    public DataExtractorFactory(ExtendedLoggerFactory extendedLoggerFactory){
        // create new extractors
        this.extraIngredientsDataExtractor = new DataExtractor<>(extendedLoggerFactory,
                ExtraIngredientRawCsvLine.class);
        this.overigeProductenDataExtractor = new DataExtractor<>(extendedLoggerFactory,
                OtherProductRawCsvLine.class);
        this.pizzaIngredientsDataExtractor = new DataExtractor<>(extendedLoggerFactory,
                PizzaIngredientsRawCsvLine.class);
        this.crustsDataExtractor = new DataExtractor<>(extendedLoggerFactory,
                CrustRawCsvLine.class);
    }

    public DataExtractor<OtherProductRawCsvLine> getOverigeProductenDataExtractor() {
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
