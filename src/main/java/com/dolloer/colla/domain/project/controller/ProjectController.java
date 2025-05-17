package com.dolloer.colla.domain.project.controller;

import com.dolloer.colla.domain.project.dto.request.CreateProjectRequest;
import com.dolloer.colla.domain.project.dto.request.InviteMembersRequest;
import com.dolloer.colla.domain.project.dto.response.ProjectResponse;
import com.dolloer.colla.domain.project.service.ProjectService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import com.dolloer.colla.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@AuthenticationPrincipal AuthUser authUser, @Valid  @RequestBody CreateProjectRequest createProjectRequest) {
        ProjectResponse projectResponse = projectService.createProject(authUser.getMember(),createProjectRequest);
        return ResponseEntity.ok(ApiResponse.success(projectResponse, ApiResponseProjectEnum.PROJECT_CREATE_SUCCESS.getMessage()));
    }

    @PostMapping("/{projectId}/invite") // 프로젝트 초대
    public ResponseEntity<ApiResponse<Void>> inviteProject(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long projectId, @RequestBody InviteMembersRequest inviteMembersRequest){
        projectService.inviteProject(authUser.getMember(), projectId ,inviteMembersRequest.getMemberIds());
        return ResponseEntity.ok(ApiResponse.success(ApiResponseProjectEnum.PROJECT_CREATE_SUCCESS.getMessage()));
    }


//    @GetMapping
//    public ResponseEntity<ApiResponse<ProjectSummaryResponse>> getProjectList(@AuthenticationPrincipal AuthUser authUser) {
//        ProjectSummaryResponse projectSummaryResponse = projectService.getProjectList(authUser.getMember());
//        return ResponseEntity.ok(ApiResponse.success(projectSummaryResponse, ApiResponseProjectEnum.PROJECT_LIST_GET_SUCCESS.getMessage()));
//    }

}
