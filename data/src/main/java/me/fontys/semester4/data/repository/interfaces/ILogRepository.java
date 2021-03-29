package me.fontys.semester4.data.repository.interfaces;

import java.util.Date;
import java.util.List;

public interface ILogRepository<LogEntryType>  {
    Iterable<LogEntryType> findAllBylogentrytime(Date time);
    LogEntryType save(LogEntryType entry);
    <T extends LogEntryType> List<T> saveAll(Iterable<T> entries);
}
