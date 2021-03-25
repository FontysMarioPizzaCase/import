package me.fontys.semester4.dominos.configuration.data.catalog.logging;


import me.fontys.semester4.data.entity.LogDetailsEntry;
import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.repository.LogDetailsEntryRepository;
import me.fontys.semester4.data.repository.LogEntryRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.LogEntryFactories.LogDetailsEntryFactory;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.LogEntryFactories.LogEntryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoggerFactory {

    private final LogEntryRepository logEntryRepository;
    private final LogDetailsEntryRepository logDetailsEntryRepository;
    private final LogEntryFactory logEntryFactory;
    private final LogDetailsEntryFactory logDetailsEntryFactory;

    @Autowired
    public DatabaseLoggerFactory(LogEntryRepository logEntryRepository,
                                 LogDetailsEntryRepository logDetailsEntryRepository,
                                 LogEntryFactory logEntryFactory,
                                 LogDetailsEntryFactory logDetailsEntryFactory){
        this.logEntryRepository = logEntryRepository;
        this.logDetailsEntryRepository = logDetailsEntryRepository;
        this.logEntryFactory = logEntryFactory;
        this.logDetailsEntryFactory = logDetailsEntryFactory;
    }

    public DatabaseLogger<LogEntry> newDatabaseLogger(String loggerName){
        return new DatabaseLogger<>(loggerName, logEntryRepository, logEntryFactory, new Report());
    }

    public DatabaseLogger<LogDetailsEntry> newDbDetailsLogger(String loggerName){
        return new DatabaseLogger<>(loggerName, logDetailsEntryRepository, logDetailsEntryFactory, new Report());
    }
}
