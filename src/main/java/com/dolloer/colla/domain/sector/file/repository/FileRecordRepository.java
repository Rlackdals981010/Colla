package com.dolloer.colla.domain.sector.file.repository;

import com.dolloer.colla.domain.sector.file.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
    List<FileRecord> findAllByProjectId(Long id);
}