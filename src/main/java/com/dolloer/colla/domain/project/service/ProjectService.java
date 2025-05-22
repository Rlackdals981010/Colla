package com.dolloer.colla.domain.project.service;



import com.dolloer.colla.domain.auth.repository.AuthRepository;
import com.dolloer.colla.domain.mail.serivce.MailService;
import com.dolloer.colla.domain.project.dto.request.ChangeRoleRequest;
import com.dolloer.colla.domain.project.dto.request.CreateProjectRequest;
import com.dolloer.colla.domain.project.dto.request.UpdateProjectRequest;
import com.dolloer.colla.domain.project.dto.response.*;
import com.dolloer.colla.domain.project.entity.InvitationStatus;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final String BASE_URL = "http://localhost:8080";


    private final ProjectRepository projectRepository;
    private final AuthRepository authRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MailService mailService;

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

    // 이메일 기반 회원 검색
    public MemberSearchResponse searchMemberByEmailForInvite(Member requester, Long projectId, String email) {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember requesterRelation = checkRelation(project, requester);

        if (requesterRelation.getRole() != ProjectRole.OWNER && requesterRelation.getRole() != ProjectRole.OWNER) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        Member found = authRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.MEMBER_NOT_EXIST));

        return new MemberSearchResponse(found.getId(), found.getUsername(), found.getEmail());
    }

    // 초대 메일 전송
    @Transactional
    public void inviteMembers(Member inviter, Long projectId, List<Long> memberIds) throws MessagingException {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember inviterRelation = checkRelation(project, inviter);

        if (!hasInvitePermission(inviterRelation)) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        for (Long memberId : memberIds) {
            Member invitee = authRepository.findById(memberId)
                    .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.MEMBER_NOT_EXIST));

            // Check if invitee is already a member or already invited
            boolean alreadyRelated = projectMemberRepository.findByProjectAndMember(project, invitee).isPresent();
            if (alreadyRelated) {
                continue; // Skip if already a member or already invited
            }

            ProjectMember newRelation = new ProjectMember(project, invitee, inviter, ProjectRole.MEMBER); // 이러면 status 자동 pending
            projectMemberRepository.save(newRelation);

            String inviteUrl = BASE_URL + "/project/" + projectId + "/invitations";
            mailService.sendHtmlInvitationEmail(invitee.getEmail(), project.getName(), inviteUrl);

        }
    }

    private boolean hasInvitePermission(ProjectMember relation) {
        return relation.getRole() == ProjectRole.OWNER || relation.getRole() == ProjectRole.OWNER;
    }

    @Transactional
    public void acceptInvitation(Member requester, Long projectId) {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember relation = checkRelation(project, requester);

        if (relation.getStatus() != InvitationStatus.PENDING) {
            throw new CustomException(ApiResponseProjectEnum.ALREADY_RESPONDED);
        }

        relation.accept();
    }

    @Transactional
    public void rejectInvitation(Member requester, Long projectId) {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember relation = checkRelation(project, requester);

        if (relation.getStatus() != InvitationStatus.PENDING) {
            throw new CustomException(ApiResponseProjectEnum.ALREADY_RESPONDED);
        }

        relation.reject();
    }

    // 멤버가 속한 프로젝트를 전체 반환
    public ProjectListResponse getProjectList(Member member) {
        List<ProjectMember> projectMemberList= projectMemberRepository.findAllByMember(member);

        List<ProjectSummaryResponse> summaryList = projectMemberList.stream()
                .map(pm -> {
                    Project project = pm.getProject();
                    return new ProjectSummaryResponse(
                            project.getId(),
                            project.getName(),
                            project.getDescription(),
                            project.getStartDate(),
                            project.getEndDate()
                    );
                })
                .toList();

        return new ProjectListResponse(summaryList);

    }

    public ProjectSummaryResponse getProject(Member member, Long projectId) {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember relation = checkRelation(project, member);

        return new ProjectSummaryResponse(project.getId(),project.getName(),project.getDescription(),project.getStartDate(),project.getEndDate());
    }

    @Transactional
    public void leaveProject(Member member, Long projectId) {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember relation = checkRelation(project, member);

        if(relation.getRole()==ProjectRole.OWNER){
            throw new CustomException(ApiResponseProjectEnum.OWNER_CANNOT_LEAVE);
        }

        // 탈퇴
        projectMemberRepository.delete(relation);
    }

    @Transactional
    public ProjectSummaryResponse updateProject(Member member, Long projectId, UpdateProjectRequest updateProjectRequest) {

        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember relation = checkRelation(project, member);

        if (relation.getRole() != ProjectRole.OWNER && relation.getRole() != ProjectRole.OWNER) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        // null 아닌 값만 업데이트
        if (updateProjectRequest.getName() != null) project.setName(updateProjectRequest.getName());
        if (updateProjectRequest.getDescription() != null) project.setDescription(updateProjectRequest.getDescription());
        if (updateProjectRequest.getStartDate() != null) project.setStartDate(updateProjectRequest.getStartDate());
        if (updateProjectRequest.getEndDate() != null) project.setEndDate(updateProjectRequest.getEndDate());

        return new ProjectSummaryResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        );

    }

    @Transactional
    public void deleteProject(Member member, Long projectId) {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember relation = checkRelation(project, member);

        if (relation.getRole() != ProjectRole.OWNER) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        projectRepository.delete(project);
    }

    public MemberListResponse getProjectMembers(Member member, Long projectId) {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember relation = checkRelation(project, member);

        // 해당 프로젝트와 관계있는 멤버들
        List<ProjectMember> projectMember = projectMemberRepository.findAllByProject(project);

        List<ProjectMembersResponse> memberSearchResponses = projectMember.stream()
                .filter(pm->pm.getStatus()!=InvitationStatus.REJECTED)
                .map( pm->{
                    Member pmMember = pm.getMember();
                    return new ProjectMembersResponse(
                            pmMember.getId(),
                            pmMember.getUsername(),
                            pmMember.getEmail(),
                            pm.getRole()
                    );
                })
                .toList();

        return new MemberListResponse(memberSearchResponses);
    }

    // 멤버 강퇴 및 초대 취소
    @Transactional
    public void deleteProjectMember(Member member, Long projectId, Long memberId) {
        // 존재하는 프로젝트
        Project project = checkProject(projectId);

        // 해당 프로젝트에 소속되어있는지
        ProjectMember relation = checkRelation(project, member);

        // 멤버 권한 체크, 일반 사용자는 강퇴 불가
        if(relation.getRole()==ProjectRole.MEMBER){
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        // 삭제 대상 멤버 확인
        Member deleteMember = authRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ApiResponseAuthEnum.MEMBER_NOT_EXIST));

        // 삭제 대상 멤버의 프로젝트 소속 확인
        ProjectMember target = projectMemberRepository.findByProjectAndMember(project, deleteMember)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_PROJECT_MEMBER));

        // 자기 자신 강퇴 방지
        if (member.getId().equals(deleteMember.getId())) {
            throw new CustomException(ApiResponseProjectEnum.CANNOT_REMOVE_SELF);
        }

        // OWNER 강퇴 방지
        if (target.getRole() == ProjectRole.OWNER) {
            throw new CustomException(ApiResponseProjectEnum.CANNOT_REMOVE_OWNER);
        }

        // 삭제
        projectMemberRepository.delete(target);
    }


    // 초대 받은 프로젝트 조회
    public ProjectInvitedListResponse getInvitedProject(Member member) {
        List<ProjectMember> projectMemberList = projectMemberRepository.findAllByMember(member);

        List<ProjectSummaryResponse> projectInvitedListResponseList = projectMemberList.stream()
                .filter(pm->pm.getStatus()==InvitationStatus.PENDING)
                .map( pm->{
                    Project project = pm.getProject();
                    return new ProjectSummaryResponse(
                            project.getId(),
                            project.getName(),
                            project.getDescription(),
                            project.getStartDate(),
                            project.getEndDate()
                    );
                })
                .toList();

        return new ProjectInvitedListResponse(projectInvitedListResponseList);
    }

    // 멤버 권한 변경
    @Transactional
    public void changeRole(Member member, Long projectId, Long memberId, ChangeRoleRequest changeRoleRequest) {
        Project project = checkProject(projectId);
        ProjectMember relation = checkRelation(project, member);

        if (memberId.equals(member.getId())) {
            throw new CustomException(ApiResponseProjectEnum.CANNOT_CHANGE_OWN_ROLE);
        }

        ProjectRole newRole = changeRoleRequest.getRole();

        // 멤버는 권한 변경 불가
        if (relation.getRole() == ProjectRole.MEMBER) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        Member target = authRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ApiResponseAuthEnum.MEMBER_NOT_EXIST));

        ProjectMember targetRelation = checkRelation(project, target);

        // 로그인 한 사람이 변경할 역할보다 권한이 낮으면 불가능
        if (!relation.getRole().hasHigherPermissionThan(targetRelation.getRole())) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        // 이미 해당 권한이면
        if (targetRelation.getRole() == newRole) {
            throw new CustomException(ApiResponseProjectEnum.ROLE_ALREADY_ASSIGNED);
        }

        // OWNER의 권한을 박탈할 수 없음
        if (targetRelation.getRole() == ProjectRole.OWNER && newRole != ProjectRole.OWNER) {
            throw new CustomException(ApiResponseProjectEnum.CANNOT_DOWNGRADE_OWNER);
        }

        targetRelation.changeRole(newRole);
    }

    // 프로젝트 존재 확인
    private Project checkProject(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));
    }

    // 유저가 프로젝트에 속한지 확인
    private ProjectMember checkRelation(Project project,Member member ){
        return projectMemberRepository.findByProjectAndMember(project, member)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_PROJECT_MEMBER));
    }
}

