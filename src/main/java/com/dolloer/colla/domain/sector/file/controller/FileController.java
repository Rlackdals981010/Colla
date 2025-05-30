package com.dolloer.colla.domain.sector.file.controller;

import com.dolloer.colla.domain.sector.file.dto.response.FileListResponse;
import com.dolloer.colla.domain.sector.file.service.FileService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseFileEnum;
import com.dolloer.colla.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    // 파일 업로드
    @PostMapping("/upload/{projectId}")
    public ResponseEntity<String> upload(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file,
            @RequestParam String principalName,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        try {
            fileService.uploadFile(projectId, file, principalName,authUser.getMember());
            return ResponseEntity.ok(ApiResponseFileEnum.FIlE_UPLOAD_SUCCESS.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("업로드 실패: " + e.getMessage());
        }
    }

    // 프로젝트 소속 링크 글 전체 보기
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<FileListResponse>> getFileList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        FileListResponse result = fileService.getFileList(authUser.getMember(), projectId);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseFileEnum.FIlE_LIST_GET_SUCCESS.getMessage()));
    }
}