package me.fontys.semester4.dominos.configuration.data.catalog.dataparsers.util;

import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLogger;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.DatabaseLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatorUtilFactory {
    private final DatabaseLoggerFactory databaseLoggerFactory;

    @Autowired
    public ValidatorUtilFactory(DatabaseLoggerFactory databaseLoggerFactory){
        this.databaseLoggerFactory = databaseLoggerFactory;
    }

    public ValidatorUtil getValidatorUtil(String parentName){
        return new ValidatorUtil(databaseLoggerFactory, parentName);
    }
}
