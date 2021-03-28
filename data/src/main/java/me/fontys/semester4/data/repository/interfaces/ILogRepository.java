package me.fontys.semester4.data.repository.interfaces;

import java.util.Date;

public interface ILogRepository<LogEntryType>  {
    Iterable<LogEntryType> findAllBylogentrytime(Date time);
    LogEntryType save(LogEntryType entry);
}
