package me.fontys.semester4.dominos.configuration.data.catalog.extractors;

import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataExtractorFactory {
    private final DataExtractor<ExtraIngredientRawCsvLine> extraIngredientsDataExtractor;
    private final DataExtractor<OtherProductRawCsvLine> overigeProductenDataExtractor;
    private final DataExtractor<PizzaIngredientsRawCsvLine> pizzaIngredientsDataExtractor;
    private final DataExtractor<CrustRawCsvLine> crustsDataExtractor;

    public DataExtractorFactory(DatabaseLoggerFactory databaseLoggerFactory){
        // create new extractors
        this.extraIngredientsDataExtractor = new DataExtractor<>(databaseLoggerFactory,
                ExtraIngredientRawCsvLine.class);
        this.overigeProductenDataExtractor = new DataExtractor<>(databaseLoggerFactory,
                OtherProductRawCsvLine.class);
        this.pizzaIngredientsDataExtractor = new DataExtractor<>(databaseLoggerFactory,
                PizzaIngredientsRawCsvLine.class);
        this.crustsDataExtractor = new DataExtractor<>(databaseLoggerFactory,
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
