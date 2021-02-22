package me.fontys.semester4.dominos.configuration.data.catalog;

import me.fontys.semester4.dominos.configuration.data.catalog.prepare.DataPrepper;
import me.fontys.semester4.dominos.configuration.data.catalog.load.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.util.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Configuration
public abstract class CsvImporter<RawT, CleanT> implements HasExtendedLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CsvImporter.class);
    protected final ExtendedLogger extendedLogger;

    private final Resource[] resources;
    private final DataPrepper<RawT, CleanT> dataPrepper;
    protected DatabaseLoader loader;

    public CsvImporter(ExtendedLoggerFactory extendedLoggerFactory,
                       @Qualifier("pizzaWithIngredients") Resource[] resources,
                       DataPrepper<RawT, CleanT> dataPrepper, DatabaseLoader loader) {
        this.extendedLogger = extendedLoggerFactory.get(LOGGER);
        this.resources = resources;
        this.dataPrepper = dataPrepper;
        this.loader = loader;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        LOGGER.info("Start import of catalog items");
        extendedLogger.clearWarnings();

        List<CleanT> csvLines = dataPrepper.prep(resources);
        doImport(csvLines);
    }


    protected void doImport(List<CleanT> csvLines) {
        LOGGER.info(String.format("Converting and importing %s csv lines...", csvLines.size()));
        loader.clearWarnings();

        for (var line : csvLines) {
            try {
                transformAndLoad(line);
            } catch (Exception e) {
                extendedLogger.processWarning(String.format("Invalid data in line: %s || ERROR: %s",
                        line.toString(), e.toString()));
                throw e; // dev
            }
        }
    }

    protected abstract void transformAndLoad(CleanT l);

    @Override
    public void report() {
        dataPrepper.report();
        loader.report();
        extendedLogger.report();
    }
}
