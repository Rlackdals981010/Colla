package com.dolloer.colla.domain.sector.file.service;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.domain.sector.file.dto.response.FileDetailResponse;
import com.dolloer.colla.domain.sector.file.dto.response.FileListResponse;
import com.dolloer.colla.domain.sector.file.dto.response.FileResponse;
import com.dolloer.colla.domain.sector.file.entity.FileRecord;
import com.dolloer.colla.domain.sector.file.repository.FileRecordRepository;
import com.dolloer.colla.googledrive.GoogleDriveService;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseFileEnum;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final GoogleDriveService googleDriveService;
    private final FileRecordRepository fileRecordRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public void uploadFile(Long projectId, MultipartFile file, String title, String description, Member member) throws IOException, GeneralSecurityException {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        // 서비스 계정 기반 업로드
        String googleDriveFileId = googleDriveService.uploadFileAndReturnFileId(file);

        FileRecord fileRecord = FileRecord.builder()
                .fileName(Optional.ofNullable(file.getOriginalFilename()).orElse("unnamed"))
                .googleDriveFileId(googleDriveFileId)
                .title(title)
                .description(description)
                .projectId(projectId)
                .uploader(member)
                .uploadedAt(LocalDateTime.now())
                .build();

        fileRecordRepository.save(fileRecord);
    }

    public FileListResponse getFileList(Member member, Long projectId) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        List<FileRecord> files = fileRecordRepository.findAllByProjectId(project.getId());

        List<FileResponse> fileList = files.stream()
                .map(file -> new FileResponse(
                        file.getId(),
                        file.getFileName(),
                        file.getTitle(),
                        file.getUploader().getId(),
                        file.getUploadedAt(),
                        file.getGoogleDriveFileId()
                )).toList();

        return new FileListResponse(fileList);
    }

    public ByteArrayOutputStream downloadFile(Long projectId, Long fileRecordId, Member member) throws IOException, GeneralSecurityException {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        FileRecord fileRecord = fileRecordRepository.findById(fileRecordId)
                .orElseThrow(() -> new CustomException(ApiResponseFileEnum.FILE_NOT_FOUND));

        if (!fileRecord.getProjectId().equals(projectId)) {
            throw new CustomException(ApiResponseFileEnum.FILE_PROJECT_MISMATCH);
        }

        // 서비스 계정 기반 다운로드
        return googleDriveService.downloadFile(fileRecord.getGoogleDriveFileId());
    }

    @Transactional
    public void deleteFile(Member member, Long projectId, Long fileId) throws GeneralSecurityException, IOException {
        Project project = checkProject(projectId);
        ProjectMember projectMember = checkRelation(project, member);

        FileRecord fileRecord = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ApiResponseFileEnum.FILE_NOT_FOUND));

        boolean isUploader = member.getId().equals(fileRecord.getUploader().getId());
        boolean isManager = projectMember.getRole() == ProjectRole.OWNER || projectMember.getRole() == ProjectRole.ADMIN;

        if (!isUploader && !isManager) {
            throw new CustomException(ApiResponseFileEnum.NOT_ENOUGH_PERMISSION);
        }

        googleDriveService.deleteFile(fileRecord.getGoogleDriveFileId());
        fileRecordRepository.delete(fileRecord);
    }

    @Transactional
    public void updateFile(Long projectId, Long fileId, Member member,
                           MultipartFile newFile, String title, String description) throws IOException, GeneralSecurityException {

        if ((newFile == null || newFile.isEmpty()) &&
                (title == null || title.isBlank()) &&
                (description == null || description.isBlank())) {
            throw new CustomException(ApiResponseFileEnum.NO_UPDATE_REQUESTED);
        }

        Project project = checkProject(projectId);
        ProjectMember projectMember = checkRelation(project, member);

        FileRecord fileRecord = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ApiResponseFileEnum.FILE_NOT_FOUND));

        boolean isUploader = member.getId().equals(fileRecord.getUploader().getId());
        boolean isManager = projectMember.getRole() == ProjectRole.OWNER || projectMember.getRole() == ProjectRole.ADMIN;

        if (!isUploader && !isManager) {
            throw new CustomException(ApiResponseFileEnum.NOT_ENOUGH_PERMISSION);
        }

        // 파일 내용 교체
        if (newFile != null && !newFile.isEmpty()) {
            // 기존 파일 삭제
            try {
                googleDriveService.deleteFile(fileRecord.getGoogleDriveFileId());
            } catch (Exception e) {
                log.warn("기존 파일 삭제 실패 (무시하고 새 파일 업로드 진행): {}", e.getMessage());
            }

            // 새 파일 업로드
            String newFileId = googleDriveService.uploadFileAndReturnFileId(newFile);
            fileRecord.updateGoogleDriveFileId(newFileId);
        }

        // 제목 수정
        if (title != null && !title.isBlank()) {
            fileRecord.updateTitle(title);
        }

        // 설명 수정
        if (description != null && !description.isBlank()) {
            fileRecord.updateDescription(description);
        }
    }

    public FileDetailResponse detailFile(Long projectId, Long fileId, Member member) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        FileRecord fileRecord = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ApiResponseFileEnum.FILE_NOT_FOUND));


        return new FileDetailResponse(fileId, fileRecord.getFileName(), fileRecord.getTitle(), fileRecord.getDescription(), fileRecord.getUploader().getId(), fileRecord.getUploadedAt());
    }

    // 검색
    public FileListResponse searchFilesByTitle(Member member, Long projectId, String keyword) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        List<FileRecord> files = fileRecordRepository.searchByTitle(project.getId(), keyword);

        List<FileResponse> fileList = files.stream()
                .map(file -> new FileResponse(
                        file.getId(),
                        file.getFileName(),
                        file.getTitle(),
                        file.getUploader().getId(),
                        file.getUploadedAt(),
                        file.getGoogleDriveFileId()
                )).toList();

        return new FileListResponse(fileList);
    }

    private Project checkProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));
    }

    private ProjectMember checkRelation(Project project, Member member) {
        return projectMemberRepository.findByProjectAndMember(project, member)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_THIS_PROJECT_MEMBER));
    }
}