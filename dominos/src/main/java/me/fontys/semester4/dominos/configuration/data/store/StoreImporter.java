package me.fontys.semester4.dominos.configuration.data.store;

import me.fontys.semester4.data.entity.Store;
import me.fontys.semester4.data.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Configuration
public class StoreImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreImporter.class);


    @Qualifier("stores")
    private final Resource[] stores;
    private final StoreRepository storeRepository;
    private final Map<String, Integer> warnings = new HashMap<>();

    @Autowired
    public StoreImporter(Resource[] stores, StoreRepository storeRepository) {
        this.stores = stores;
        this.storeRepository = storeRepository;
    }

    public void doImport() throws IOException {
        LOGGER.info("Starting import of stores...");

        this.warnings.clear();
        List<Store> stores = new ArrayList<>();

        for (Resource resource : this.stores) {
            LOGGER.info("Reading resource " + resource.getFilename());
            stores.addAll(this.processStoreResource(resource));
        }

        LOGGER.info(String.format("Inserting %s stores...", stores.size()));
        this.storeRepository.saveAll(stores);
    }

    public void report() {
        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Store import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }

    private List<Store> processStoreResource(Resource resource) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        List<String> allStrings = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        while(true) {
            String currentLine = in.readLine();
            if (currentLine == null) {
                break;
            } else if (currentLine.isEmpty()) {
                if (str.length() > 0) {
                    allStrings.add(str.toString());
                }
                str = new StringBuilder();
            } else {
                if (str.length() == 0) {
                    str = new StringBuilder(currentLine);
                } else {
                    str.append("\n").append(currentLine);
                }
            }
        }
        if (str.length() > 0) {
            allStrings.add(str.toString());
        }
        List<Store> stores = new ArrayList<>();
        allStrings.forEach((_store) -> {
            String[] lines = _store.split("\\r?\\n");
            // TODO I know store length is 7, what happens if this changes?
            //   should improve in future
            if (lines.length < 7) {
                this.processWarning(String.format("Process store %s went wrong", _store));
                return;
            }
            String storeName = lines[0];
            String storeStreet = String.format("%s %s", lines[1], lines[2]);
            stores.add(new Store(null, storeName, storeStreet));
        });
        return stores;
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }
}
