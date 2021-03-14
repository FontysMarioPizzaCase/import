package me.fontys.semester4.dominos.configuration.data.catalog.general;

import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.util.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public class DataPrepper<RawT, CleanT> implements HasExtendedLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DataPrepper.class);
    protected final ExtendedLogger extendedLogger;

    private final DataExtractor<RawT> dataExtractor;
    protected DataValidator<RawT> validator;
    protected DataCleaner<RawT, CleanT> cleaner;

    public DataPrepper(ExtendedLoggerFactory extendedLoggerFactory, DataExtractor<RawT> dataExtractor,
                       DataValidator<RawT> validator, DataCleaner<RawT, CleanT> cleaner) {
        this.extendedLogger = extendedLoggerFactory.extendedLogger(LOGGER);
        this.dataExtractor = dataExtractor;
        this.validator = validator;
        this.cleaner = cleaner;
    }

    public List<CleanT> prep(Resource[] resources) throws IOException {
        LOGGER.info("Prepare input");
        extendedLogger.clearWarnings();

        List<RawT> rawCsvLines = dataExtractor.extractRaw(resources);
        validator.validate(rawCsvLines);
        return cleaner.clean(rawCsvLines);
    }

    public void report(){
        dataExtractor.report();
        validator.report();
        extendedLogger.report();
    }
}
