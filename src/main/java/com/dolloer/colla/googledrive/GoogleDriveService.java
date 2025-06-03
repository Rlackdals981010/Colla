package com.dolloer.colla.googledrive;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveService {

    private static final String APPLICATION_NAME = "Colla";

    @Value("${gcp.service-account.path}")
    private String SERVICE_ACCOUNT_JSON_PATH;

    private Drive getDriveService() throws IOException, GeneralSecurityException {
        GoogleCredential credential = GoogleCredential
                .fromStream(new ClassPathResource(SERVICE_ACCOUNT_JSON_PATH.replace("classpath:", "")).getInputStream())
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive"));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName(APPLICATION_NAME).build();
    }

    // 파일 업로드 (ID 반환)
    public String uploadFileAndReturnFileId(MultipartFile multipartFile)
            throws IOException, GeneralSecurityException {

        Drive driveService = getDriveService();

        File fileMetadata = new File();
        fileMetadata.setName(multipartFile.getOriginalFilename());

        java.io.File tempFile = java.io.File.createTempFile("upload-", multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);
        FileContent mediaContent = new FileContent(multipartFile.getContentType(), tempFile);

        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        tempFile.delete();
        return uploadedFile.getId();
    }

    // 파일 다운로드
    public ByteArrayOutputStream downloadFile(String googleDriveFileId)
            throws IOException, GeneralSecurityException {

        Drive driveService = getDriveService();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveService.files().get(googleDriveFileId).executeMediaAndDownloadTo(outputStream);
        return outputStream;
    }
}