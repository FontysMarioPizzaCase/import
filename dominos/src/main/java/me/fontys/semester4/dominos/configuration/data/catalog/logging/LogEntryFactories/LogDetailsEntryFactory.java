package me.fontys.semester4.dominos.configuration.data.catalog.logging.LogEntryFactories;

import me.fontys.semester4.data.entity.LogDetailsEntry;
import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.entity.Severity;
import org.springframework.stereotype.Service;

@Service
public class LogDetailsEntryFactory implements ILogEntryFactory<LogDetailsEntry>{
    @Override
    public LogDetailsEntry newEntry(String message, Severity severity, String loggerName) {
        return new LogDetailsEntry(message, severity, loggerName);
    }
}
