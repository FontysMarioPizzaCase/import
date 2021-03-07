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

        // query db
        Optional<Ingredient> temp = this.repository.findByName(record.getIngredientName());

        if(temp.isPresent()){
            ingredient = temp.get();
            // no props to set
            processWarning("Ingredient updated");
        }
        else {
            ingredient = new Ingredient(null, record.getIngredientName(), null);
            this.repository.save(ingredient);
            processWarning("Ingredient created");
        }
        buffer.add(ingredient);

        return ingredient;
    }

    private Ingredient findInBuffer(String ingredientName) {
        for (var ingredient : buffer) {
            if (ingredientName.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Added or updated %s ingredients", this.buffer.size()));

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
