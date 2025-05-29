package com.dolloer.colla.googledrive.service;

import com.dolloer.colla.oauth.entity.OAuthToken;
import com.dolloer.colla.oauth.repository.OAuthTokenRepository;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleDriveService {

    private final OAuthTokenRepository tokenRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

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

    public void uploadFileUsingSavedToken(String principalName) throws IOException, GeneralSecurityException {
        OAuthToken token = tokenRepository.findByProviderAndPrincipalName("google", principalName)
                .orElseThrow(() -> new RuntimeException("No token found"));

        try {
            uploadFile(token.getAccessToken());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 401) {
                String newAccessToken = refreshAccessToken(token);
                uploadFile(newAccessToken);
            } else {
                throw e;
            }
        }
    }

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