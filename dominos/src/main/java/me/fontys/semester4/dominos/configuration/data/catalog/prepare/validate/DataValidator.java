package me.fontys.semester4.dominos.configuration.data.catalog.prepare.validate;

import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.util.ExtendedLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.util.HasExtendedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class DataValidator<RawT> implements HasExtendedLogger {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DataValidator.class);
    protected final ExtendedLogger extendedLogger;

    public DataValidator(ExtendedLoggerFactory extendedLoggerFactory){
        this.extendedLogger = extendedLoggerFactory.get(LOGGER);
    }

    public void validate(List<RawT> rawLines) {
        LOGGER.info("- Validating...");
        extendedLogger.clearWarnings();

        for (RawT line : rawLines) {
            validate(line);
        }
    }

    protected abstract void validate(RawT line);

    public void report(){
        extendedLogger.report();
    }
}
