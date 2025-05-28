package com.dolloer.colla.googledrive.controller;

import com.dolloer.colla.googledrive.service.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DriveController {

    private final GoogleDriveService googleDriveService;

    @GetMapping("/test/upload")
    public ResponseEntity<String> uploadTest(@RequestParam String accessToken) {
        try {
            googleDriveService.uploadFile(accessToken);
            return ResponseEntity.ok("업로드 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패: " + e.getMessage());
        }
    }
}