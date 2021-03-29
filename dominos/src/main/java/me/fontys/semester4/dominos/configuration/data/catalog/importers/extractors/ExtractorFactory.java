package me.fontys.semester4.dominos.configuration.data.catalog.importers.extractors;

import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.ExtraIngredientRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.OtherProductRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.PizzaIngredientsRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExtractorFactory {
    private final Extractor<ExtraIngredientRawCsvLine> extraIngredientsExtractor;
    private final Extractor<OtherProductRawCsvLine> overigeProductenExtractor;
    private final Extractor<PizzaIngredientsRawCsvLine> pizzaIngredientsExtractor;
    private final Extractor<CrustRawCsvLine> crustsExtractor;

    public ExtractorFactory(DatabaseLoggerFactory databaseLoggerFactory){
        // create new extractors
        this.extraIngredientsExtractor = new Extractor<>(databaseLoggerFactory,
                ExtraIngredientRawCsvLine.class);
        this.overigeProductenExtractor = new Extractor<>(databaseLoggerFactory,
                OtherProductRawCsvLine.class);
        this.pizzaIngredientsExtractor = new Extractor<>(databaseLoggerFactory,
                PizzaIngredientsRawCsvLine.class);
        this.crustsExtractor = new Extractor<>(databaseLoggerFactory,
                CrustRawCsvLine.class);
    }

    public Extractor<OtherProductRawCsvLine> getOverigeProductenDataExtractor() {
        return overigeProductenExtractor;
    }

    public Extractor<ExtraIngredientRawCsvLine> getExtraIngredientsDataExtractor() {
        return extraIngredientsExtractor;
    }

    public Extractor<PizzaIngredientsRawCsvLine> getPizzaIngredientsDataExtractor() {
        return pizzaIngredientsExtractor;
    }

    public Extractor<CrustRawCsvLine> getCrustsDataExtractor() {
        return crustsExtractor;
    }
}
