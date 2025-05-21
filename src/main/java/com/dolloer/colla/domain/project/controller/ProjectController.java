package com.dolloer.colla.domain.project.controller;

import com.dolloer.colla.domain.project.dto.request.CreateProjectRequest;
import com.dolloer.colla.domain.project.dto.request.InviteMembersRequest;
import com.dolloer.colla.domain.project.dto.request.UpdateProjectRequest;
import com.dolloer.colla.domain.project.dto.response.*;
import com.dolloer.colla.domain.project.service.ProjectService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import com.dolloer.colla.security.AuthUser;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
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

    // 이메일 기반 멤버 검색 (초대 대상 필터링)
    @GetMapping("/{projectId}/invite/search")
    public ResponseEntity<ApiResponse<MemberSearchResponse>> searchMemberForInvite(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestParam String email
    ) {
        MemberSearchResponse result = projectService.searchMemberByEmailForInvite(authUser.getMember(), projectId, email);
        return ResponseEntity.ok(ApiResponse.success(result, "초대 대상 검색 완료"));
    }

    // 초대 메일 전송
    @PostMapping("/{projectId}/invite")
    public ResponseEntity<ApiResponse<Void>> inviteProject(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestBody InviteMembersRequest inviteMembersRequest
    ) throws MessagingException {
        projectService.inviteMembers(authUser.getMember(), projectId, inviteMembersRequest.getMemberIds());
        return ResponseEntity.ok(ApiResponse.success(ApiResponseProjectEnum.PROJECT_INVITE_SUCCESS.getMessage()));
    }

    @PostMapping("/{projectId}/invitations/accept")
    public ResponseEntity<ApiResponse<Void>> acceptInvitation(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId) {

        projectService.acceptInvitation(authUser.getMember(), projectId);
        return ResponseEntity.ok(ApiResponse.success("프로젝트 초대를 수락했습니다."));
    }

    @PostMapping("/{projectId}/invitations/reject")
    public ResponseEntity<ApiResponse<Void>> rejectInvitation(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId) {

        projectService.rejectInvitation(authUser.getMember(), projectId);
        return ResponseEntity.ok(ApiResponse.success("프로젝트 초대를 거절했습니다."));
    }

    // 내가 속한 프로젝트 전체 조회
    @GetMapping("")
    public  ResponseEntity<ApiResponse<ProjectListResponse>> getProjectList(@AuthenticationPrincipal AuthUser authUser){
        ProjectListResponse result = projectService.getProjectList(authUser.getMember());
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseProjectEnum.PROJECT_LIST_GET_SUCCESS.getMessage()));
    }

    // 입장 하려는 프로젝트 세부 내용
    @GetMapping("{projectId}")
    public ResponseEntity<ApiResponse<ProjectSummaryResponse>> getProject(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        ProjectSummaryResponse result = projectService.getProject(authUser.getMember(), projectId);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseProjectEnum.PROJECT_GET_SUCCESS.getMessage()));
    }

    // 프로젝트 탈퇴
    @DeleteMapping("{projectId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveProject(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        projectService.leaveProject(authUser.getMember(),projectId);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseProjectEnum.PROJECT_LEAVE_SUCCESS.getMessage()));
    }

    // 프로젝트 수정
    @PutMapping("{projectId}")
    public ResponseEntity<ApiResponse<ProjectSummaryResponse>> updateProject(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestBody UpdateProjectRequest updateProjectRequest
    ) {
        ProjectSummaryResponse result =projectService.updateProject(authUser.getMember(),projectId, updateProjectRequest);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseProjectEnum.PROJECT_CREATE_SUCCESS.getMessage()));
    }

    // 프로젝트 삭제
    @DeleteMapping("{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        projectService.deleteProject(authUser.getMember(),projectId);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseProjectEnum.PROJECT_LEAVE_SUCCESS.getMessage()));
    }

    // 해당 프로젝트 멤버 전체 조회
    @GetMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<MemberListResponse>> getProjectMembers(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        MemberListResponse result =  projectService.getProjectMembers(authUser.getMember(),projectId);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseProjectEnum.PROJECT_MEMBERS_GET_SUCCESS.getMessage()));
    }

    // 팀원 강퇴
    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteProjectMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long memberId
    ){
        projectService.deleteProjectMember(authUser.getMember(),projectId,memberId);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseProjectEnum.PROJECT_MEMBERS_DELETE_SUCCESS.getMessage()));
    }

}

