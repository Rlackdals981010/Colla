package com.dolloer.colla.domain.sector.file.controller;

import com.dolloer.colla.domain.sector.file.dto.response.FileListResponse;
import com.dolloer.colla.domain.sector.file.service.FileService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseFileEnum;
import com.dolloer.colla.security.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@Slf4j
@RequestMapping("/project/{projectId}/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    // 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        try {
            fileService.uploadFile(projectId, file, authUser.getMember());
            return ResponseEntity.ok(ApiResponseFileEnum.FIlE_UPLOAD_SUCCESS.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("업로드 실패: " + e.getMessage());
        }
    }
//    @PostMapping("/upload")
//    public ResponseEntity<String> upload(HttpServletRequest request) {
//        System.out.println("🔥 Content-Type: " + request.getContentType());
//        System.out.println("🔥 Is Multipart: " + (request instanceof MultipartHttpServletRequest));
//        return ResponseEntity.ok("debug");
//    }

    // 파일 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<FileListResponse>> getFileList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ) {
        log.info("호출됨?");
        FileListResponse result = fileService.getFileList(authUser.getMember(), projectId);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseFileEnum.FIlE_LIST_GET_SUCCESS.getMessage()));
    }

    // 파일 다운로드
    @GetMapping("/{fileId}/download")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @AuthenticationPrincipal AuthUser authUser
    ) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream outputStream = fileService.downloadFile(projectId, fileId, authUser.getMember());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"downloaded-file\"")
                .body(outputStream.toByteArray());
    }

    // 파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long fileId
    ) {
        fileService.deleteFile(authUser.getMember(), projectId, fileId);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseFileEnum.FIlE_DELETE_SUCCESS.getMessage()));
    }
}