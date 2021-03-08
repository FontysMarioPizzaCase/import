package me.fontys.semester4.dominos.configuration.data.catalog.extraingredientsurcharge;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.dominos.configuration.data.catalog.entityimporters.IngredientImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ExtraIngedientSurchargeImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtraIngedientSurchargeImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final Resource[] resources;
    private final IngredientImporter ingredientImporter;

    @Autowired
    public ExtraIngedientSurchargeImporter(@Qualifier("ingredientSurcharge") Resource[] resources,
                                           IngredientImporter ingredientImporter) {
        this.resources = resources;
        this.ingredientImporter = ingredientImporter;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        LOGGER.info("Starting import of ingredient surcharge records...");
        this.warnings.clear();

        List<ExtraIngredientSurchargeRecord> records = processResources();
        importEntities(records);
    }

    private List<ExtraIngredientSurchargeRecord> processResources() throws IOException {
        List<ExtraIngredientSurchargeRecord> records = new ArrayList<>();

        for (Resource resource : this.resources) {
            LOGGER.info("Reading resource " + resource.getFilename());
            records.addAll(this.processCsv(resource));
        }
        return records;
    }

    private List<ExtraIngredientSurchargeRecord> processCsv(Resource resource) throws IOException {
        final char separator = ';';

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resource.getInputStream()));

        CsvToBean<ExtraIngredientSurchargeRecord> beans =
                new CsvToBeanBuilder<ExtraIngredientSurchargeRecord>(reader)
                .withSeparator(separator)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .withIgnoreLeadingWhiteSpace(true)
                .withType(ExtraIngredientSurchargeRecord.class)
                .withThrowExceptions(false)
                .build();
        List<ExtraIngredientSurchargeRecord> records = beans.parse();

        beans.getCapturedExceptions().forEach((e) -> processWarning("Inconsistent data: " + e));

        return records;
    }

    private void importEntities(List<ExtraIngredientSurchargeRecord> records) {
        LOGGER.info(String.format("Converting %s records...", records.size()));

        for (var record : records) {
            try {
                ingredientImporter.extractAndImport(record);
            } catch (Exception e) {
                processWarning(String.format("Invalid data in record: %s || ERROR: %s",
                        record.toString(), e.toString()));
            }
        }
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        ingredientImporter.report();

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Catalog import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
