package me.fontys.semester4.dominos.configuration.data.catalog.logging.LogEntryFactories;

import me.fontys.semester4.data.entity.Severity;

public interface ILogEntryFactory<LogEntryType>{
    LogEntryType newEntry(String message, Severity severity, String loggerName);
}
