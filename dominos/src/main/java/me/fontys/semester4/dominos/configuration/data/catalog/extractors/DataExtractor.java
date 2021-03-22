package me.fontys.semester4.dominos.configuration.data.catalog.extractors;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataExtractor<RawT> implements HasExtendedLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DataExtractor.class);
    protected final ExtendedLogger extendedLogger;

    private final Class<RawT> type;

    public DataExtractor(ExtendedLoggerFactory extendedLoggerFactory, Class<RawT> type) {
        this.extendedLogger = extendedLoggerFactory.extendedLogger(LOGGER);
        this.type = type;
    }

    public List<RawT> extractRaw(Resource[] resources) throws IOException {
        extendedLogger.clearWarnings();

        List<RawT> rawCsvLines = new ArrayList<>();
        for (Resource resource : resources) {
            LOGGER.info("- Extracting from " + resource.getFilename());
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
                (e) -> extendedLogger.processWarning("Inconsistent data: " + e));

        return rawCsvLines;
    }

    public void report(){
        extendedLogger.report();
    }
}

