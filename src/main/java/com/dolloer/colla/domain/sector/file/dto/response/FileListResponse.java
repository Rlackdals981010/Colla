package com.dolloer.colla.domain.sector.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FileListResponse {
    List<FileResponse> linkListResponses;
}