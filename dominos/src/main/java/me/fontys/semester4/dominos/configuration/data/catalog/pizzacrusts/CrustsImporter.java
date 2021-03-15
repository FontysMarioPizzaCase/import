package me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts;

import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.dominos.configuration.data.catalog.general.CsvImporter;
import me.fontys.semester4.dominos.configuration.data.catalog.general.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzacrusts.csv_models.CrustRawCsvLine;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CrustsImporter extends CsvImporter<CrustRawCsvLine, CrustCsvLine> {
    private final Map<Long, Ingredient> crusts;

    @Autowired
    public CrustsImporter(ExtendedLoggerFactory extendedLoggerFactory,
                          @Qualifier("pizzacrusts") Resource[] resources,
                          CrustsDataExtractor dataExtractor,
                          CrustsDataValidator validator, CrustsDataCleaner cleaner,
                          DatabaseLoader loader) {
        super(extendedLoggerFactory, resources, dataExtractor, validator, cleaner, loader);
        this.crusts = new HashMap<>();
    }

    @Override
    protected void transformAndLoad(CrustCsvLine l) {
        Ingredient crust = new Ingredient(null, l.getCrustName(), l.getDescription(), l.getAddPrice(),
                l.getSize(), l.isAvailable());

        // cache crust
        crust = loader.toDb(crust);
        crusts.put(crust.getIngredientid(), crust);

        // TODO: ingredient categories
    }
}
