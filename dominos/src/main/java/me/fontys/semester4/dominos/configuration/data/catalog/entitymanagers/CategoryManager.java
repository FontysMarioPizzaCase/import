package me.fontys.semester4.dominos.configuration.data.catalog.entitymanagers;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.pizzawithingredients.PizzaAndIngredientRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CategoryManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryManager.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Category> buffer;
    private final CategoryRepository repository;

    public CategoryManager(CategoryRepository repository) {
        this.repository = repository;
        this.buffer = new ArrayList<>();
    }

    public List<Category> extractAndImport(PizzaAndIngredientRecord record) {
        Category parent = extractAndImport(record.getCategoryName());
        Category child = extractAndImport(record.getSubCategoryName(), parent);

        return new ArrayList<>(List.of(parent, child));
    }

    private Category extractAndImport(String name, Category parent) {
        Category child = extractAndImport(name);
        child.setParent(parent);
        return child;
    }

    private Category extractAndImport(String name) {
        Category category = findInBuffer(name);

        if (category != null) {
            processWarning("Category already processed. Skipped.");
            return category;
        }
        if (name.isEmpty()) {
            processWarning("Record does not have a category name");
            throw new IllegalArgumentException();
        }

        category = saveNewOrUpdate(name);
        buffer.add(category);

        return category;
    }

    private Category findInBuffer(String categoryName) {
        for (var category : buffer) {
            if (categoryName.equals(category.getName())) {
                return category;
            }
        }
        return null;
    }

    private Category saveNewOrUpdate(String name) {
        Category category;
        Optional<Category> temp = this.repository.findByName(name);

        if(temp.isPresent()){
            category = temp.get();
            processWarning("No Category properties to update");
        }
        else {
            category = saveNewCategory(name);
            processWarning("Category created");
        }
        return category;
    }

    private Category saveNewCategory(String name) {
        Category category = new Category(null, null, name);
        this.repository.save(category);
        return category;
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Added or updated %s categories", this.buffer.size()));

        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Category import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }
}
