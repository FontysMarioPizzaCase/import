package me.fontys.semester4.dominos.configuration.data.catalog.logging;


import me.fontys.semester4.data.repository.ImportLogEntryRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtendedLoggerFactory {

    private final ImportLogEntryRepository logRepository;

    @Autowired
    public ExtendedLoggerFactory(ImportLogEntryRepository logRepository){
        this.logRepository = logRepository;
    }

    public DatabaseLogger extendedLogger(Logger logger){
        return new DatabaseLogger(logger, logRepository);
    }
}
