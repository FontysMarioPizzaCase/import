package me.fontys.semester4.dominos.configuration.data.catalog.importers.repohelper;

import me.fontys.semester4.data.entity.Category;
import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.data.repository.CategoryRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;

import java.util.Optional;

public class CategoryRepoHelper {
    protected final DatabaseLogger log;
    private final CategoryRepository categoryRepository;

    public CategoryRepoHelper(DatabaseLogger databaseLogger, CategoryRepository categoryRepository) {
        this.log = databaseLogger;
        this.categoryRepository = categoryRepository;
    }

    public Category saveOrUpdate(Category newData) {
        Optional<Category> temp = this.categoryRepository.findByNameIgnoreCase(newData.getName());

        Category category;
        if (temp.isPresent()) {
            category = temp.get();
            updateProperties(category, newData);
        } else {
            category = newData;
            saveEntity(category);
        }

        return category;
    }

    private void saveEntity(Category category) {
        this.categoryRepository.save(category);
        log.addToReport("Created category " + category, Severity.INFO);
    }

    private void updateProperties(Category category, Category newData) {
              // placeholder, no properties to update
    }
}
