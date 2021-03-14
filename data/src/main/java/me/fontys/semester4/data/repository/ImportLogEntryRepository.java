package me.fontys.semester4.data.repository;

import me.fontys.semester4.data.entity.ImportLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImportLogEntryRepository extends JpaRepository<ImportLogEntry, Long>, JpaSpecificationExecutor<ImportLogEntry>
{
}
