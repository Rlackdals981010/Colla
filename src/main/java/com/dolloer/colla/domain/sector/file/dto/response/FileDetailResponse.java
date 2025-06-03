package com.dolloer.colla.domain.sector.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FileDetailResponse {
    private Long fileId;
    private String fileName;
    private String title;
    private String description;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;
}