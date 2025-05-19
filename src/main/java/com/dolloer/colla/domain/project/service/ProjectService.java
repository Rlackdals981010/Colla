package com.dolloer.colla.domain.project.service;



import com.dolloer.colla.domain.auth.repository.AuthRepository;
import com.dolloer.colla.domain.mail.serivce.MailService;
import com.dolloer.colla.domain.project.dto.request.CreateProjectRequest;
import com.dolloer.colla.domain.project.dto.response.MemberSearchResponse;
import com.dolloer.colla.domain.project.dto.response.ProjectResponse;
import com.dolloer.colla.domain.project.entity.InvitationStatus;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.response.exception.CustomException;
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
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));

        ProjectMember requesterRelation = projectMemberRepository.findByProjectAndMember(project, requester)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_PROJECT_MEMBER));

        if (requesterRelation.getRole() != ProjectRole.OWNER && requesterRelation.getRole() != ProjectRole.ADMIN) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        Member found = authRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.MEMBER_NOT_EXIST));

        return new MemberSearchResponse(found.getId(), found.getUsername(), found.getEmail());
    }

    // 초대 메일 전송
    @Transactional
    public void inviteMembers(Member inviter, Long projectId, List<Long> memberIds) throws MessagingException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));

        ProjectMember inviterRelation = projectMemberRepository.findByProjectAndMember(project, inviter)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_PROJECT_MEMBER));

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
        return relation.getRole() == ProjectRole.OWNER || relation.getRole() == ProjectRole.ADMIN;
    }

    @Transactional
    public void acceptInvitation(Member requester, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));

        ProjectMember relation = projectMemberRepository.findByProjectAndMember(project, requester)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_INVITED_MEMBER));

        if (relation.getStatus() != InvitationStatus.PENDING) {
            throw new CustomException(ApiResponseProjectEnum.ALREADY_RESPONDED);
        }

        relation.accept();
    }

    @Transactional
    public void rejectInvitation(Member requester, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));

        ProjectMember relation = projectMemberRepository.findByProjectAndMember(project, requester)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_INVITED_MEMBER));

        if (relation.getStatus() != InvitationStatus.PENDING) {
            throw new CustomException(ApiResponseProjectEnum.ALREADY_RESPONDED);
        }

        relation.reject();
    }




}

