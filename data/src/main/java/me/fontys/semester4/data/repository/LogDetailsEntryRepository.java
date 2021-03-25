package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.LogDetailsEntry;
import me.fontys.semester4.data.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface LogDetailsEntryRepository extends ILogRepository<LogDetailsEntry>, JpaRepository<LogDetailsEntry, Long>, JpaSpecificationExecutor<LogDetailsEntry> {

    Iterable<LogDetailsEntry> findAllBylogentrytime(Date time);
}
