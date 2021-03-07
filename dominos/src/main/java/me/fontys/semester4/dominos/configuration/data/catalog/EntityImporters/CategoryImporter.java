package me.fontys.semester4.dominos.configuration.data.catalog.EntityImporters;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CategoryImporter<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Category> buffer;
    private final CategoryRepository repository;

    public CategoryImporter(CategoryRepository repository) {
        this.repository = repository;
        this.buffer = new ArrayList<>();
    }

    @Transactional
    public List<Category> importCategories(PizzaAndIngredientRecord record) {
        Category parent = getCategory(record.getCategoryName());
        Category child = getCategory(record.getSubCategoryName(), parent);

        List<Category> bothCategories = new ArrayList<>();
        bothCategories.add(parent);
        bothCategories.add(child);

        this.repository.saveAll(bothCategories);

        return bothCategories;
    }

    private Category getCategory(String name, Category parent) {
        Category child = getCategory(name);
        child.setParent(parent);
        return child;
    }

    private Category getCategory(String name) {
        if (name.isEmpty()) {
            processWarning("Record does not have a category name");
            return null;
        }
        if (existsInBuffer(name)) {
            processWarning("Category already processed");
            return null;
        }

        // query db
        Optional<Category> temp = this.repository.findByName(name);
        Category category;

        if(temp.isPresent()){
            category = temp.get();
            // no props to set
        }
        else {
            category = new Category(null, null, name);
        }

        buffer.add(category);

        return category;
    }

    private boolean existsInBuffer(String categoryName) {
        for (var category : buffer) {
            if (categoryName.equals(category.getName())) {
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
        LOGGER.info(String.format("Imported %s categories", this.buffer.size()));

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
