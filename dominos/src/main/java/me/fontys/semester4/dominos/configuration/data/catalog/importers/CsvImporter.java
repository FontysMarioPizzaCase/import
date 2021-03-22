package me.fontys.semester4.dominos.configuration.data.catalog.importers;

import me.fontys.semester4.dominos.configuration.data.catalog.datacleaners.DataCleaner;
import me.fontys.semester4.dominos.configuration.data.catalog.datavalidators.DataValidator;
import me.fontys.semester4.dominos.configuration.data.catalog.extractors.DataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.dataloader.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Configuration
public abstract class CsvImporter<RawT, CleanT> implements Importer {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CsvImporter.class);
    protected final ExtendedLogger extendedLogger;
    protected final Environment environment;

    private final Resource[] resources;
    private final DataExtractor<RawT> dataExtractor;
    protected DataValidator<RawT> validator;
    protected DataCleaner<RawT, CleanT> cleaner;
    protected DatabaseLoader loader;

    public CsvImporter(Environment environment,
                       ExtendedLoggerFactory extendedLoggerFactory,
                       Resource[] resources, DataExtractor<RawT> dataExtractor,
                       DataValidator<RawT> validator, DataCleaner<RawT, CleanT> cleaner,
                       DatabaseLoader loader) {
        this.environment = environment;
        this.extendedLogger = extendedLoggerFactory.extendedLogger(LOGGER);
        this.resources = resources;
        this.dataExtractor = dataExtractor;
        this.validator = validator;
        this.cleaner = cleaner;
        this.loader = loader;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        announce();
        extendedLogger.clearWarnings();

        List<RawT> rawCsvLines = dataExtractor.extractRaw(resources);
        List<RawT> acceptedLines = validator.validate(rawCsvLines);
        List<CleanT> cleanedLines = cleaner.clean(acceptedLines);
        doImport(cleanedLines);
        loadCachedRelationships();
    }

    protected void announce() {
        StringBuilder sb = new StringBuilder();
        for (Resource r : resources) {
            sb.append(r.getFilename()).append(" ");
        }
        LOGGER.info(String.format("Start import of %s", sb.toString()));
    }

    protected void doImport(List<CleanT> csvLines) {
        LOGGER.info(String.format("- Transforming and importing %s csv lines...", csvLines.size()));
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

    protected void loadCachedRelationships() {
        // optional override
    }

    @Override
    public void report() {
        dataExtractor.report();
        validator.report();
        loader.report();
        extendedLogger.report();
    }
}
