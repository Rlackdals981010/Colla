package com.dolloer.colla.domain.sector.file.entity;

import com.dolloer.colla.domain.auth.entity.Member;
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

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member uploader;

    private LocalDateTime uploadedAt;

    public void updateTitle(String title) {
        this.fileName = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateGoogleDriveFileId(String newFileId) {
        this.googleDriveFileId = newFileId;
    }
}