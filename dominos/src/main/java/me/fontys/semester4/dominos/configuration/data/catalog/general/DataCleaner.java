package me.fontys.semester4.dominos.configuration.data.catalog.general;

import me.fontys.semester4.dominos.configuration.data.catalog.util.CleanerUtil;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.util.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class DataCleaner<RawT, CleanT> implements HasExtendedLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DataCleaner.class);
    private final ExtendedLogger extendedLogger;

    protected final CleanerUtil util;


    public DataCleaner(ExtendedLoggerFactory extendedLoggerFactory, CleanerUtil util) {
        this.extendedLogger = extendedLoggerFactory.extendedLogger(LOGGER);
        this.util = util;
    }

    public List<CleanT> clean(List<RawT> rawCsvLines) {
        LOGGER.info("- Cleaning data...");
        extendedLogger.clearWarnings();


        List<CleanT> cleanedLines = new ArrayList<>();

        for (RawT raw : rawCsvLines) {
            cleanedLines.add(clean(raw));
        }

        return cleanedLines;
    }

    protected abstract CleanT clean(RawT raw);

    public void report(){
        extendedLogger.report();
    }
}
