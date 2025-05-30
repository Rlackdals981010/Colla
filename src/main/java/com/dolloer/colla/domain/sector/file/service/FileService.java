package com.dolloer.colla.domain.sector.file.service;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.domain.sector.file.dto.response.FileListResponse;
import com.dolloer.colla.domain.sector.file.dto.response.FileResponse;
import com.dolloer.colla.domain.sector.file.entity.FileRecord;
import com.dolloer.colla.domain.sector.file.repository.FileRecordRepository;
import com.dolloer.colla.googledrive.service.GoogleDriveService;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final GoogleDriveService googleDriveService;
    private final FileRecordRepository fileRecordRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public void uploadFile(Long projectId, MultipartFile file, String principalName, Member member) throws IOException, GeneralSecurityException {
        Project project = checkProject(projectId);
        checkRelation(project,member);

        // 실제 업로드
        String googleDriveFileId = googleDriveService.uploadFileAndReturnFileId(file, principalName);

        // 메타데이터 저장
        FileRecord fileRecord = FileRecord.builder()
                .fileName(Optional.ofNullable(file.getOriginalFilename()).orElse("unnamed"))
                .googleDriveFileId(googleDriveFileId)
                .projectId(projectId)
                .uploadedBy(principalName)
                .uploadedAt(LocalDateTime.now())
                .build();

        fileRecordRepository.save(fileRecord);
    }

    public FileListResponse getFileList(Member member, Long projectId) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        List<FileRecord> files = fileRecordRepository.findAllByProjectId(project.getId());

        List<FileResponse> fileList = files.stream()
                .map( file -> new FileResponse(
                        file.getFileName(),
                        file.getUploadedBy(),
                        file.getUploadedAt(),
                        file.getGoogleDriveFileId()
                        )
                ).toList();

        return new FileListResponse(fileList);
    }

    // 프로젝트 존재 확인
    private Project checkProject(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));
    }

    // 유저가 프로젝트에 속한지 확인
    private ProjectMember checkRelation(Project project, Member member ){
        return projectMemberRepository.findByProjectAndMember(project, member)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_THIS_PROJECT_MEMBER));
    }


}