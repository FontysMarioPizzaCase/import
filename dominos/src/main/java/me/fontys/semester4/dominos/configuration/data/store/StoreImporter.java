package me.fontys.semester4.dominos.configuration.data.store;

import me.fontys.semester4.data.entity.Store;
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

    private final Map<String, Integer> warnings = new HashMap<>();

    @Autowired
    public StoreImporter(Resource[] stores) {
        this.stores = stores;
    }

    public void doImport() throws IOException {
        LOGGER.info("Starting import of stores...");

        this.warnings.clear();
        List<String> stores = new ArrayList<>();

        for (Resource resource : this.stores) {
            LOGGER.info("Reading resource " + resource.getFilename());
            stores.addAll(this.processStoreResource(resource));
        }

        LOGGER.info(String.format("Inserting %s stores...", stores.size()));
//        this.storeRepository.saveAll(orders);
    }

    public void report() {
        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            LOGGER.warn(String.format("Order import result : %s warnings", totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                LOGGER.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }

    private List<String> processStoreResource(Resource resource) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        List<String> allStrings = new ArrayList<String>();
        String str ="";
        while(true) {
            String tmp = in.readLine();
            if (tmp == null) {
                break;
            } else if (tmp.isEmpty()) {
                if (!str.isEmpty()) {
                    allStrings.add(str);
                }
                str = "";
            } else {
                if (str.isEmpty()) {
                    str = tmp;
                } else {
                    str += "\n" + tmp;
                }
            }
        }
        if (!str.isEmpty()) {
            allStrings.add(str);
        }
        System.out.println(allStrings.size());
        allStrings.forEach((_store) -> {
            String lines[] = _store.split("\\r?\\n");
            System.out.println(lines);
            System.out.println(Arrays.toString(lines));
            System.exit(0);
        });
        return allStrings;
    }

    private void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }
}
