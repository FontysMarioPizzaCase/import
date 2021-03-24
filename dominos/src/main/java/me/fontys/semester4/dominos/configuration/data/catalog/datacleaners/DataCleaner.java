package me.fontys.semester4.dominos.configuration.data.catalog.datacleaners;

import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class DataCleaner<RawT, CleanT> implements HasExtendedLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DataCleaner.class);
    private final DatabaseLogger log;

    protected final CleanerUtil util;


    public DataCleaner(ExtendedLoggerFactory extendedLoggerFactory, CleanerUtil util) {
        this.log = extendedLoggerFactory.extendedLogger(LOGGER);
        this.util = util;
    }

    public List<CleanT> clean(List<RawT> rawCsvLines) {
        log.info("- Cleaning data...");
        log.clearReport();


        List<CleanT> cleanedLines = new ArrayList<>();

        for (RawT raw : rawCsvLines) {
            cleanedLines.add(clean(raw));
        }

        return cleanedLines;
    }

    protected abstract CleanT clean(RawT raw);

    public void report(){
        log.report();
    }
}
