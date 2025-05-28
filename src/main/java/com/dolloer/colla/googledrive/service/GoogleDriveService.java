package com.dolloer.colla.googledrive.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class GoogleDriveService {
    public void uploadFile(String accessToken) throws IOException, GeneralSecurityException {
        HttpRequestInitializer credential = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        Drive drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("Colla")
                .build();

        File fileMetadata = new File();
        fileMetadata.setName("test.txt");

        java.io.File filePath = new java.io.File("/Users/kcm/Desktop/Colla/src/main/resources/test.txt");
        FileContent mediaContent = new FileContent("text/plain", filePath);

        drive.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
    }
}