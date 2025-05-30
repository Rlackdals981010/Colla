package com.dolloer.colla.domain.sector.file.service;

import com.dolloer.colla.domain.sector.file.entity.FileRecord;
import com.dolloer.colla.domain.sector.file.repository.FileRecordRepository;
import com.dolloer.colla.googledrive.service.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileService {

    private final GoogleDriveService googleDriveService;
    private final FileRecordRepository fileRecordRepository;

    public void uploadFile(MultipartFile file, String principalName) throws IOException, GeneralSecurityException {
        // 실제 업로드
        String googleDriveFileId = googleDriveService.uploadFileAndReturnFileId(file, principalName);

        // 메타데이터 저장
        FileRecord fileRecord = FileRecord.builder()
                .fileName(file.getOriginalFilename())
                .googleDriveFileId(googleDriveFileId)
                .uploadedBy(principalName)
                .uploadedAt(LocalDateTime.now())
                .build();

        fileRecordRepository.save(fileRecord);
    }
}