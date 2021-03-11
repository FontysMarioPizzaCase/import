package me.fontys.semester4.dominos.configuration.data.catalog.extract;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.dominos.configuration.data.catalog.CatalogImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogImporter.class);
    private final Map<String, Integer> warnings = new HashMap<>();

    private CleanerUtil cleanerUtil;

    public DataExtractor(CleanerUtil cleanerUtil) {
        this.cleanerUtil = cleanerUtil;
    }

    public List<CsvLine> extract(Resource[] resources) throws IOException {
        LOGGER.info("Extracting and validating input...");
        this.warnings.clear();

        List<RawCsvLine> rawCsvLines = extractRaw(resources);
        validate(rawCsvLines);
        return clean(rawCsvLines);
    }

    private List<RawCsvLine> extractRaw(Resource[] resources) throws IOException {
        List<RawCsvLine> rawCsvLines = new ArrayList<>();
        for (Resource resource : resources) {
            LOGGER.info("Reading resource " + resource.getFilename());
            rawCsvLines.addAll(this.extractRaw(resource));
        }
        return rawCsvLines;
    }

    private List<RawCsvLine> extractRaw(Resource resource) throws IOException {
        final char DELIMITER = ';';

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resource.getInputStream()));

        CsvToBean<RawCsvLine> beans = new CsvToBeanBuilder<RawCsvLine>(reader)
                .withSeparator(DELIMITER)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .withIgnoreLeadingWhiteSpace(true)
                .withType(RawCsvLine.class)
                .withThrowExceptions(false)
                .build();
        List<RawCsvLine> rawCsvLines = beans.parse();

        beans.getCapturedExceptions().forEach((e) -> processWarning("Inconsistent data: " + e));

        return rawCsvLines;
    }

    private void validate(List<RawCsvLine> rawLines) {
        for (RawCsvLine line : rawLines) {
            validate(line);
        }
    }

    private void validate(RawCsvLine line){
        if (line.getIngredientName().isEmpty()) {
            processWarning("Record does not have an ingredient name");
            throw new IllegalArgumentException();
        }
        if (line.getCategoryName().isEmpty()) {
            processWarning("Record does not have a category name");
            throw new IllegalArgumentException();
        }
        if (line.getProductName().isEmpty()) {
            processWarning("Record does not have a product name");
            throw new IllegalArgumentException();
        }
        if (line.getPrice().isEmpty()) {
            processWarning("Record does not have a product price");
            throw new IllegalArgumentException();
        }
        if (line.getProductDescription().isEmpty()) {
            processWarning("Record does not have a product description");
        }
        if (line.getIsSpicy().isEmpty()) {
            processWarning("Record does not have a product spicy indicator");
        }
        if (line.getIsVegetarian().isEmpty()) {
            processWarning("Record does not have a product vegetarian indicator");
        }
        if (line.getDeliveryFee().isEmpty()) {
            processWarning("Record does not have a product delivery fee");
        }
    }

    private List<CsvLine> clean(List<RawCsvLine> rawCsvLines) {
        List<CsvLine> csvLines = new ArrayList<>();

        for (RawCsvLine raw : rawCsvLines) {
            String categoryName = cleanerUtil.cleanString(raw.getCategoryName());
            String subCategoryName = cleanerUtil.cleanString(raw.getSubCategoryName());
            String productName = cleanerUtil.cleanString(raw.getProductName());
            String productDescription = cleanerUtil.cleanString(raw.getProductDescription());
            BigDecimal price = cleanerUtil.cleanPrice(raw.getPrice());
            BigDecimal deliveryFee = cleanerUtil.cleanPrice(raw.getDeliveryFee());
            boolean isSpicy = raw.getIsSpicy().equalsIgnoreCase("JA");
            boolean isVegetarian = raw.getIsVegetarian().equalsIgnoreCase("JA");
            boolean isAvailable = raw.getIsVegetarian().equalsIgnoreCase("JA");
            int ingredientPortion = Integer.valueOf(raw.getIngredientPortion());
            String ingredientName = cleanerUtil.cleanString(raw.getIngredientName());
            String standardPizzasauce = cleanerUtil.cleanString(raw.getStandardPizzasauce());
            double taxrate = 0.06; // TODO: input taxrate?

            csvLines.add(new CsvLine(categoryName, subCategoryName, productName,
                    productDescription, price, deliveryFee, isSpicy, isVegetarian, isAvailable,
                    ingredientPortion, ingredientName, standardPizzasauce));
        }

        return csvLines;
    }







    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
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
