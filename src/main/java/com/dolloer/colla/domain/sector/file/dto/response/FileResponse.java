package com.dolloer.colla.domain.sector.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FileResponse {
    private Long fileId;
    private String fileName;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;
    private String googleDriveFileId;
}