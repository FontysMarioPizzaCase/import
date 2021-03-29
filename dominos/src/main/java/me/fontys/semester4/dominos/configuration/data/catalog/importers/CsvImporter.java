package me.fontys.semester4.dominos.configuration.data.catalog.importers;

import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.parsers.Parser;
import me.fontys.semester4.dominos.configuration.data.catalog.importers.extractors.Extractor;
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
    private final Extractor<RawT> extractor;
    protected Parser<RawT, CleanT> parser;

    public CsvImporter(Environment environment,
                       DatabaseLoggerFactory databaseLoggerFactory,
                       Resource[] resources, Extractor<RawT> extractor,
                       Parser<RawT, CleanT> parser) {
        this.log = databaseLoggerFactory.newDatabaseLogger(this.getClass().getSuperclass().getName());
        this.environment = environment;
        this.resources = resources;
        this.extractor = extractor;
        this.parser = parser;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doImport() throws IOException {
        announce();
        log.clearReport();

        List<RawT> rawCsvLines = extractor.extractRaw(resources);
        List<CleanT> cleanedLines = parser.parse(rawCsvLines);
        try {
            transformAndLoad(cleanedLines);
            loadCachedRelationships();
        } catch (Exception e) {
            log.addToReport(
                    String.format("Could not continue import. Rolling back. || ERROR: %s", e.toString()),
                    Severity.ERROR);
            throw e; // TODO: look up how to roll back
        }
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
        extractor.report();
        parser.report();
        log.report();
    }
}
