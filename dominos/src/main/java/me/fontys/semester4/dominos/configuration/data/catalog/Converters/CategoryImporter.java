package me.fontys.semester4.dominos.configuration.data.catalog.Converters;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.Ingredient;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.data.repository.IngredientRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.PizzaAndIngredientRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryImporter<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryImporter.class);

    private final Map<String, Integer> warnings = new HashMap<>();
    private final List<Category> categories;
    private final CategoryRepository repository;

    public List<Category> getCategories() {
        return categories;
    }

    public CategoryImporter(CategoryRepository repository) {
        this.repository = repository;
        this.categories = new ArrayList<>();
    }

    public List<Category> process(PizzaAndIngredientRecord record) {
        List<Category> cats = new ArrayList<>();
        cats.add(processCategory(record.getCategory(), record));
        cats.add(processCategory(record.getSubCategory(), record));
        cats.get(1).setParent(cats.get(0));

        return cats;
    }

    private Category processCategory(String name, PizzaAndIngredientRecord record) {
        if (name.isEmpty()) {
            processWarning("Record does not have a category name");
            return null;
        }
        if (existsByName(name)) {
            processWarning("Category already processed");
            return null;
        }

        Category newCategory = new Category(
                null,
                null,
                record.getCategory()
        );
        categories.add(newCategory);

        // TODO: find and save per one

        return newCategory;
    }

    private boolean existsByName(String categoryName) {
        for (var category : categories) {
            if (categoryName.equals(category.getName())) {
                return true;
            }
        }
        return false;
    }

    public void saveAll() {
        this.repository.saveAll(this.categories);
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        LOGGER.info(String.format("Converted %s categories", this.categories.size()));

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
