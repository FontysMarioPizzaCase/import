package me.fontys.semester4.dominos.configuration.data.catalog.importers;

import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.dataloader.DatabaseLoaderFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.DataParser;
import me.fontys.semester4.dominos.configuration.data.catalog.extractors.DataExtractor;
import me.fontys.semester4.dominos.configuration.data.catalog.dataloader.DatabaseLoader;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Configuration
public abstract class CsvImporter<RawT, CleanT> implements Importer {
    protected final DatabaseLogger log;
    protected final Environment environment;

    private final Resource[] resources;
    private final DataExtractor<RawT> dataExtractor;
    protected DataParser<RawT, CleanT> parser;
    protected DatabaseLoader loader;

    public CsvImporter(Environment environment,
                       DatabaseLoggerFactory databaseLoggerFactory,
                       Resource[] resources, DataExtractor<RawT> dataExtractor,
                       DataParser<RawT, CleanT> parser,
                       DatabaseLoaderFactory databaseLoaderFactory) {
        this.log = databaseLoggerFactory.newDatabaseLogger(this.getClass().getSuperclass().getName());
        this.environment = environment;
        this.resources = resources;
        this.dataExtractor = dataExtractor;
        this.parser = parser;
        this.loader = databaseLoaderFactory.getDatabaseLoader();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        announce();
        log.clearReport();

        List<RawT> rawCsvLines = dataExtractor.extractRaw(resources);
        List<CleanT> cleanedLines = parser.parse(rawCsvLines);
        transformAndLoad(cleanedLines);
        loadCachedRelationships();
    }

    protected void announce() {
        StringBuilder sb = new StringBuilder();
        for (Resource r : resources) {
            sb.append(r.getFilename()).append(" ");
        }
        log.info(String.format("Start import of %s", sb.toString()));
    }

    protected void transformAndLoad(List<CleanT> csvLines) {
        log.info(String.format("- Transforming and importing %s csv lines...", csvLines.size()));
        loader.clearWarnings();

        for (var line : csvLines) {
            try {
                transformAndLoad(line);
            } catch (Exception e) {
                log.addToReport(
                        String.format("Invalid data in line: %s || ERROR: %s", line.toString(), e.toString()),
                        Severity.ERROR);
                throw e; // FIXME (dev)
            }
        }
    }

    protected abstract void transformAndLoad(CleanT l);

    protected void loadCachedRelationships() {
        // optional override
    }

    @Override
    public void report() {
        dataExtractor.report(); // nothing?
        parser.report();
        loader.report();
        log.report(); // nothing?

    }
}
