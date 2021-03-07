package me.fontys.semester4.dominos.configuration.data.catalog.EntityImporters;

import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class IngredientImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Ingredient> buffer;
    private final IngredientRepository repository;

    public IngredientImporter(IngredientRepository repository) {
        this.repository = repository;
        this.buffer = new ArrayList<>();
    }

    @Transactional
    public Ingredient importIngredient(PizzaAndIngredientRecord record) {
        Ingredient ingredient = getIngredient(record);
        this.repository.save(ingredient);

        return ingredient;
    }

    private Ingredient getIngredient(PizzaAndIngredientRecord record) {
        if (record.getIngredientName().isEmpty()) {
            processWarning("Record does not have an ingredient name");
            return null;
        }
        if (existsInBuffer(record.getIngredientName())) {
            processWarning("Ingredient already processed");
            return null;
        }

        // query db
        Optional<Ingredient> temp = this.repository.findByName(record.getProductName());
        Ingredient ingredient;

        if(temp.isPresent()){
            ingredient = temp.get();
            // no props to set
        }
        else {
            ingredient = new Ingredient(
                    null,
                    record.getIngredientName(),
                    null
            );
        }
        buffer.add(ingredient);

        return ingredient;
    }

    private boolean existsInBuffer(String ingredientName) {
        for (var ingredient : buffer) {
            if (ingredientName.equals(ingredient.getName())) {
                return true;
            }
        }
        return false;
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Imported %s ingredients", this.buffer.size()));

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
