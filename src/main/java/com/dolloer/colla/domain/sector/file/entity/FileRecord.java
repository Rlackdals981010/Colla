package com.dolloer.colla.domain.sector.file.entity;

import com.dolloer.colla.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String googleDriveFileId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    private String uploadedBy; // principalName or colla 내부 userId

    private LocalDateTime uploadedAt;
}