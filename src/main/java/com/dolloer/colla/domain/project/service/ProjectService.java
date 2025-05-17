package com.dolloer.colla.domain.project.service;



import com.dolloer.colla.domain.project.dto.request.CreateProjectRequest;
import com.dolloer.colla.domain.project.dto.response.ProjectResponse;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectResponse createProject(Member creator, CreateProjectRequest request) {
        // 1. 프로젝트 생성
        Project project = new Project(
                request.getName(),
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate()
        );
        projectRepository.save(project);

        // 2. 생성자를 OWNER로 등록
        ProjectMember ownerRelation = new ProjectMember(
                project,
                creator,
                creator, // invitedBy = 본인
                ProjectRole.OWNER
        );
        ownerRelation.accept(); // 상태를 ACCEPTED로 명시

        projectMemberRepository.save(ownerRelation);

        // 3. 응답 반환
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        );
    }


    public void inviteProject(Member member, Long projectId, List<Long> memberIds) {
        // 1. 프로젝트 존재 여부 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));

        // 2. 초대자가 이 프로젝트에 소속되어 있는지 확인
        ProjectMember inviterRelation = projectMemberRepository.findByProjectAndMember(project, member)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_PROJECT_MEMBER));

        // 3. 초대자의 권한이 OWNER 또는 ADMIN인지 확인
        if (inviterRelation.getRole() != ProjectRole.OWNER && inviterRelation.getRole() != ProjectRole.ADMIN) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }



    }
}
