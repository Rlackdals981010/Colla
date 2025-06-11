package com.dolloer.colla.domain.sector.file.repository;

import com.dolloer.colla.domain.sector.file.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
    List<FileRecord> findAllByProjectId(Long id);

    @Query("SELECT f FROM FileRecord f WHERE f.projectId = :projectId AND LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<FileRecord> searchByTitle(@Param("projectId") Long projectId, @Param("keyword") String keyword);
}