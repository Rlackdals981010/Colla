package com.dolloer.colla.googledrive.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    // 저장된 refreshtoken으로 accesstoken을 재발급 받고 db에 갱신
    public String refreshAccessToken(OAuthToken token) {
        try {
            String tokenUrl = "https://oauth2.googleapis.com/token";

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", googleClientId);
            params.add("client_secret", googleClientSecret);
            params.add("refresh_token", token.getRefreshToken());
            params.add("grant_type", "refresh_token");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String newAccessToken = (String) response.getBody().get("access_token");
                token.setAccessToken(newAccessToken);
                tokenRepository.save(token);
                return newAccessToken;
            } else {
                throw new RuntimeException("AccessToken 갱신 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("토큰 갱신 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public void uploadFileWithMultipart(MultipartFile multipartFile, String principalName)
            throws IOException, GeneralSecurityException {

        OAuthToken token = tokenRepository.findByProviderAndPrincipalName("google", principalName)
                .orElseThrow(() -> new RuntimeException("No token found"));

        String accessToken = token.getAccessToken();

        try {
            uploadMultipartToDrive(multipartFile, accessToken);
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 401) {
                String newAccessToken = refreshAccessToken(token);
                uploadMultipartToDrive(multipartFile, newAccessToken);
            } else {
                throw e;
            }
        }
    }

    private void uploadMultipartToDrive(MultipartFile multipartFile, String accessToken)
            throws IOException, GeneralSecurityException {

        HttpRequestInitializer credential = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        Drive drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Colla").build();

        File fileMetadata = new File();
        fileMetadata.setName(multipartFile.getOriginalFilename());

        java.io.File tempFile = java.io.File.createTempFile("upload-", multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);

        FileContent mediaContent = new FileContent(multipartFile.getContentType(), tempFile);

        drive.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        // 업로드 완료 후 임시 파일 삭제
        tempFile.delete();
    }

    public String uploadFileAndReturnFileId(MultipartFile multipartFile, String principalName) throws IOException, GeneralSecurityException {
        OAuthToken token = tokenRepository.findByProviderAndPrincipalName("google", principalName)
                .orElseThrow(() -> new RuntimeException("No token found"));

        try {
            return uploadMultipartToDriveAndGetId(multipartFile, token.getAccessToken());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 401) {
                String newAccessToken = refreshAccessToken(token);
                return uploadMultipartToDriveAndGetId(multipartFile, newAccessToken);
            } else {
                throw e;
            }
        }
    }

    private String uploadMultipartToDriveAndGetId(MultipartFile multipartFile, String accessToken)
            throws IOException, GeneralSecurityException {

        HttpRequestInitializer credential = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        Drive drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Colla").build();

        File fileMetadata = new File();
        fileMetadata.setName(multipartFile.getOriginalFilename());

        java.io.File tempFile = java.io.File.createTempFile("upload-", multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);

        FileContent mediaContent = new FileContent(multipartFile.getContentType(), tempFile);

        File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        // 업로드 완료 후 임시 파일 삭제
        tempFile.delete();

        return uploadedFile.getId();
    }

    public ByteArrayOutputStream downloadFile(String googleDriveFileId, String principalName) throws IOException, GeneralSecurityException {
        log.info("principalName = " + principalName);
        OAuthToken token = tokenRepository.findByProviderAndPrincipalName("google", principalName)
                .orElseThrow(() -> new RuntimeException("No token found"));

        try {
            return downloadFromDrive(googleDriveFileId, token.getAccessToken());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 401) {
                String newAccessToken = refreshAccessToken(token);
                return downloadFromDrive(googleDriveFileId, newAccessToken);
            } else {
                throw e;
            }
        }
    }

    private ByteArrayOutputStream downloadFromDrive(String fileId, String accessToken)
            throws IOException, GeneralSecurityException {

        HttpRequestInitializer credential = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        Drive drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Colla").build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        return outputStream;
    }



}