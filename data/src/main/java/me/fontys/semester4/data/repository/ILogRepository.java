package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.LogDetailsEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

public interface ILogRepository<LogEntryType>  {
    Iterable<LogEntryType> findAllBylogentrytime(Date time);
    LogEntryType save(LogEntryType entry);
}
