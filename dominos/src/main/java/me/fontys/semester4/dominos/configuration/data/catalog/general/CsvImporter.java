package me.fontys.semester4.dominos.configuration.data.catalog.general;

import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.util.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final DataExtractor<RawT> dataExtractor;
    protected DataValidator<RawT> validator;
    protected DataCleaner<RawT, CleanT> cleaner;
    protected DatabaseLoader loader;

    public CsvImporter(ExtendedLoggerFactory extendedLoggerFactory,
                       Resource[] resources, DataExtractor<RawT> dataExtractor,
                       DataValidator<RawT> validator, DataCleaner<RawT, CleanT> cleaner,
                       DatabaseLoader loader) {
        this.extendedLogger = extendedLoggerFactory.extendedLogger(LOGGER);
        this.resources = resources;
        this.dataExtractor = dataExtractor;
        this.validator = validator;
        this.cleaner = cleaner;
        this.loader = loader;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        LOGGER.info("Start import of catalog items");
        extendedLogger.clearWarnings();

        List<RawT> rawCsvLines = dataExtractor.extractRaw(resources);
        validator.validate(rawCsvLines);
        List<CleanT> csvLines = cleaner.clean(rawCsvLines);
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
        dataExtractor.report();
        validator.report();
        loader.report();
        extendedLogger.report();
    }
}
