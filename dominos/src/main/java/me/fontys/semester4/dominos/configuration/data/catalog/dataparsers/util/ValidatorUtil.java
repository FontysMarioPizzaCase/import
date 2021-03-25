package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util;

import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.models.raw_csv_models.HasProductData;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasDatabaseLogger;

public class ValidatorUtil implements HasDatabaseLogger {
    protected final DatabaseLogger<LogEntry> log;

    public ValidatorUtil(DatabaseLoggerFactory databaseLoggerFactory, String parentName) {
        String loggerName = this.getClass().getName() + "(" + parentName + ")";
        this.log = databaseLoggerFactory.newDatabaseLogger(loggerName);
    }


    public void report() {
        log.report();
    }
}
