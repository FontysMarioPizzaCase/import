package me.fontys.semester4.dominos.configuration.data.catalog.extractors;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.data.entity.LogLevel;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasDatabaseLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataExtractor<RawT> implements HasDatabaseLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DataExtractor.class);
    protected final DatabaseLogger log;

    private final Class<RawT> type;

    public DataExtractor(DatabaseLoggerFactory databaseLoggerFactory, Class<RawT> type) {
        this.log = databaseLoggerFactory.extendedLogger(LOGGER);
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
                (e) -> log.addToReport("Inconsistent data: " + e, LogLevel.ERROR));

        return rawCsvLines;
    }

    public void report(){
        log.report();
    }
}

