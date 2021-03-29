package me.fontys.semester4.dominos.configuration.data.catalog.importers.extractors;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasDatabaseLogger;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Extractor<RawT> implements HasDatabaseLogger {
    protected final DatabaseLogger log;

    private final Class<RawT> type;

    public Extractor(DatabaseLoggerFactory databaseLoggerFactory, Class<RawT> type) {
        this.log = databaseLoggerFactory.newDatabaseLogger(this.getClass().getName());
        this.type = type;
    }

    public List<RawT> extractRaw(Resource[] resources) throws IOException {
        log.clearReport();

        List<RawT> rawCsvLines = new ArrayList<>();
        for (Resource resource : resources) {
            log.info("- Extracting from " + resource.getFilename());
            rawCsvLines.addAll(this.extractRaw(resource));
        }
        return rawCsvLines;
    }

    protected List<RawT> extractRaw(Resource resource) throws IOException {
        final char DELIMITER = ';';

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resource.getInputStream()));

        CsvToBean<RawT> beans = new CsvToBeanBuilder<RawT>(reader)
                .withSeparator(DELIMITER)
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .withIgnoreLeadingWhiteSpace(true)
                .withType(this.type)
                .withThrowExceptions(false)
                .build();
        List<RawT> rawCsvLines = beans.parse();

        beans.getCapturedExceptions().forEach(
                (e) -> log.addToReport("Inconsistent data: " + e, Severity.ERROR));

        return rawCsvLines;
    }

    public void report(){
        log.report();
    }
}

