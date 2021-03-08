package me.fontys.semester4.dominos.configuration.data.catalog.entityimporters;

import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.extraingredientsurcharge.ExtraIngredientSurchargeRecord;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.PizzaAndIngredientRecord;
import me.fontys.semester4.dominos.configuration.data.catalog.util.PriceCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class IngredientImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Ingredient> buffer1;
    private final List<Ingredient> buffer2;
    private final IngredientRepository repository;
    private final PriceCleaner priceCleaner;

    public IngredientImporter(IngredientRepository repository,
                              PriceCleaner priceCleaner) {
        this.repository = repository;
        this.priceCleaner = priceCleaner;
        this.buffer1 = new ArrayList<>();
        this.buffer2 = new ArrayList<>(); // TODO: temp, will refactor
    }

    public Ingredient extractAndImport(PizzaAndIngredientRecord record) {
        Ingredient ingredient = findInBuffer(record.getIngredientName());

        if (ingredient != null) {
            processWarning("Ingredient already processed. Skipped.");
            return ingredient;
        }
        if (record.getIngredientName().isEmpty()) {
            processWarning("Record does not have an ingredient name");
            throw new IllegalArgumentException();
        }

        ingredient = saveNewOrUpdate(record);
        buffer1.add(ingredient);

        return ingredient;
    }

    // TODO: temp, will refactor
    public Ingredient extractAndImport(ExtraIngredientSurchargeRecord record) {
        Ingredient ingredient = findInBuffer2(record.getIngredientName());

        if (ingredient != null) {
            processWarning("Ingredient already processed. Skipped.");
            return ingredient;
        }
        if (record.getIngredientName().isEmpty()) {
            processWarning("Record does not have an ingredient name. Skipped.");
            throw new IllegalArgumentException();
        }
        if (record.getAddPrice().isEmpty()) {
            processWarning("Record does not have a surcharge. Skipped.");
            throw new IllegalArgumentException();
        }

        ingredient = saveNewOrUpdate(record);
        buffer2.add(ingredient);

        return ingredient;
    }

    private Ingredient findInBuffer(String ingredientName) {
        for (var ingredient : buffer1) {
            if (ingredientName.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }

    private Ingredient findInBuffer2(String ingredientName) { // TODO: temp, will refactor
        for (var ingredient : buffer2) {
            if (ingredientName.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }

    private Ingredient saveNewOrUpdate(PizzaAndIngredientRecord record) {
        Ingredient ingredient;
        Optional<Ingredient> temp = this.repository.findByName(record.getIngredientName());

        if(temp.isPresent()){
            ingredient = temp.get();
            processWarning("No Ingredient properties to update");
        }
        else {
            ingredient = saveNewIngredient(record);
            processWarning("Ingredient created");
        }
        return ingredient;
    }

    private Ingredient saveNewOrUpdate(ExtraIngredientSurchargeRecord record) { // TODO: needs refactor
        Ingredient ingredient;
        Optional<Ingredient> temp = this.repository.findByName(record.getIngredientName());

        if(temp.isPresent()){
            ingredient = temp.get();
            ingredient.setAddprice(priceCleaner.clean(record.getAddPrice()));
            processWarning("Ingredient surcharge updated");
        }
        else {
            ingredient = saveNewIngredient(record);
            processWarning("Ingredient created");
        }
        return ingredient;
    }

    private Ingredient saveNewIngredient(PizzaAndIngredientRecord record) {
        Ingredient ingredient = new Ingredient(null, record.getIngredientName(), null);
        this.repository.save(ingredient);
        return ingredient;
    }

    private Ingredient saveNewIngredient(ExtraIngredientSurchargeRecord record) { // TODO: refactor
        BigDecimal price = priceCleaner.clean(record.getAddPrice());
        Ingredient ingredient = new Ingredient(null, record.getIngredientName(), price);
        this.repository.save(ingredient);
        return ingredient;
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Added or updated %s ingredients", this.buffer1.size()));

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Ingredient import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
