package me.fontys.semester4.dominos.configuration.data.catalog.logging;


import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.repository.LogEntryRepository;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.LogEntryFactories.LogEntryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoggerFactory {

    private final LogEntryRepository logEntryRepository;
    private final LogEntryFactory logEntryFactory;

    @Autowired
    public DatabaseLoggerFactory(LogEntryRepository logEntryRepository,
                                 LogEntryFactory logEntryFactory){
        this.logEntryRepository = logEntryRepository;
        this.logEntryFactory = logEntryFactory;
    }

    public DatabaseLogger newDatabaseLogger(String loggerName){
        Report report = new Report(logEntryFactory);
        return new DatabaseLogger(loggerName, logEntryRepository, logEntryFactory, report);
    }
}
