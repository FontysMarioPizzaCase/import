package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.repository.interfaces.ILogRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface LogEntryRepository extends ILogRepository<LogEntry>, JpaRepository<LogEntry, Long>, JpaSpecificationExecutor<LogEntry> {
    Iterable<LogEntry> findAllBylogentrytime(Date time);
}
