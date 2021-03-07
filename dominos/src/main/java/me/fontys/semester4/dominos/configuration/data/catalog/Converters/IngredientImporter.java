package me.fontys.semester4.dominos.configuration.data.catalog.Converters;

import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.entity.Product;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.data.repository.ProductRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngredientImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Ingredient> ingredients;
    private final IngredientRepository repository;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public IngredientImporter(IngredientRepository repository) {
        this.repository = repository;
        this.ingredients = new ArrayList<>();
    }

    public Ingredient process(PizzaAndIngredientRecord record) {
        if (record.getIngredientName().isEmpty()) {
            processWarning("Record does not have an ingredient name");
            return null;
        }
        if (existsByName(record.getIngredientName())) {
            processWarning("Ingredient already processed");
            return null;
        }

        Ingredient newIngredient = new Ingredient(
                null,
                record.getIngredientName(),
                null
        );
        ingredients.add(newIngredient);

        return newIngredient;
    }

    private boolean existsByName(String ingredientName) {
        for (var ingredient : ingredients) {
            if (ingredientName.equals(ingredient.getName())) {
                return true;
            }
        }
        return false;
    }

    public void saveAll() {
        this.repository.saveAll(this.ingredients);
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Converted %s ingredients", this.ingredients.size()));

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Product conversion result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
