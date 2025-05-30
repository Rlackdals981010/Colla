package com.dolloer.colla.domain.sector.file.entity;

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

    private String uploadedBy; // principalName or colla 내부 userId

    private LocalDateTime uploadedAt;
}