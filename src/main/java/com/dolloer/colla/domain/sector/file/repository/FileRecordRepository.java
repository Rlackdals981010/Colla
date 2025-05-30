package com.dolloer.colla.domain.sector.file.repository;

import com.dolloer.colla.domain.sector.file.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
}