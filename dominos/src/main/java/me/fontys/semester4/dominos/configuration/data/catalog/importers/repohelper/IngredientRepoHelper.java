package me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper;

import me.fontys.semester4.data.entity.*;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;

import java.util.Optional;

public class IngredientRepoHelper {
    protected final DatabaseLogger<LogEntry> log;
    private final IngredientRepository ingredientRepository;

    public IngredientRepoHelper(DatabaseLogger<LogEntry> databaseLogger, IngredientRepository ingredientRepository) {
        this.log = databaseLogger;
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient saveOrUpdate(Ingredient newData) {
        Optional<Ingredient> temp = this.ingredientRepository.findByNameIgnoreCase(newData.getName());

        Ingredient ingredient;
        if (temp.isPresent()) {
            ingredient = temp.get();
            updateProperties(ingredient, newData);
        } else {
            ingredient = newData;
            saveEntity(ingredient);
        }

        return ingredient;
    }

    private void saveEntity(Ingredient ingredient) {
        if (ingredient.getAddprice() == null) {
            log.addToReport("Creating ingredient without price: " + ingredient, Severity.WARN);
        }
        else{
            log.addToReport("Created ingredient: " + ingredient, Severity.INFO);
        }
        this.ingredientRepository.save(ingredient);
    }

    private void updateProperties(Ingredient ingredient, Ingredient newData) {
        boolean isUpdated = false;

        StringBuilder logMessage = new StringBuilder();
        logMessage.append(String.format("Updating ingredient %s: ", ingredient.getName()));

        if (newData.getDescription() != null && !newData.getDescription().isEmpty()
                && !newData.getDescription().equals(ingredient.getDescription())) {
            logMessage.append(String.format("[%s]=>[%s] ", ingredient.getDescription(), newData.getDescription()));
            ingredient.setDescription(newData.getDescription());
            isUpdated = true;
        }
        if (newData.getSize() != null && !newData.getSize().equals(ingredient.getSize())) {
            logMessage.append(String.format("[%s]=>[%s] ", ingredient.getSize(), newData.getSize()));
            ingredient.setSize(newData.getSize());
            isUpdated = true;
        }
        if (newData.getAddprice() != null && !newData.getAddprice().equals(ingredient.getAddprice())) {
            logMessage.append(String.format("[%s]=>[%s] ", ingredient.getAddprice(), newData.getAddprice()));
            ingredient.setAddprice(newData.getAddprice());
            isUpdated = true;
        }
        if (newData.isAvailable() != null && !newData.isAvailable().equals(ingredient.isAvailable())) {
            logMessage.append(String.format("[%s]=>[%s] ", ingredient.isAvailable(), newData.isAvailable()));
            ingredient.setAvailable(newData.isAvailable());
            isUpdated = true;
        }

        if (!isUpdated) {
            return;
        }

        log.addToReport(logMessage.toString(), Severity.INFO);
    }
}
