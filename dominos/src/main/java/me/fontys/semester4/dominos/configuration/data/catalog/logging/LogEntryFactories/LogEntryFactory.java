package me.fontys.semester4.dominos.configuration.data.catalog.logging.LogEntryFactories;

import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import org.springframework.stereotype.Service;

@Service
public class LogEntryFactory implements ILogEntryFactory<LogEntry>{
    @Override
    public LogEntry newEntry(String message, Severity severity, String loggerName) {
        return new LogEntry(message, severity, loggerName);
    }
}
