package me.fontys.semester4.dominos.configuration.data.catalog.util;

import org.slf4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class ExtendedLogger {
    private final Logger logger;
    private final Map<String, Integer> warnings = new HashMap<>();

    public ExtendedLogger(Logger logger) {
        this.logger = logger;
    }

    public void processWarning(String message) {
        if (this.warnings.containsKey(message)) {
            this.warnings.put(message, this.warnings.get(message) + 1);
        } else {
            this.warnings.put(message, 1);
        }
    }

    public void report() {
        int totalWarnings = this.warnings.values().stream()
                .reduce(0, Integer::sum);

        if (totalWarnings > 0) {
            String  title = logger.getName().substring(logger.getName().lastIndexOf('.') + 1);
            logger.warn(String.format("%s result : %s warnings", title, totalWarnings));

            for (Map.Entry<String, Integer> warning : this.warnings.entrySet()) {
                logger.warn(String.format("  -> %s : %s", warning.getKey(), warning.getValue()));
            }
        }
    }

    public void clearWarnings() {
        this.warnings.clear();
    }
}
