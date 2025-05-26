package com.dolloer.colla.domain.sector.notice.service;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.domain.sector.notice.dto.request.NoticeCreateRequest;
import com.dolloer.colla.domain.sector.notice.entity.Notice;
import com.dolloer.colla.domain.sector.notice.repository.NoticeRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseNoticeEnum;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public void createNotice(Member member, Long projectId, NoticeCreateRequest request) {
        Project project = checkProject(projectId);
        ProjectMember relation = checkRelation(project, member);

        // 관리자만 생성 가능
        if (relation.getRole() == ProjectRole.MEMBER) {
            throw new CustomException(ApiResponseNoticeEnum.NOT_ENOUGH_PERMISSION);
        }

        // 중복명 불가
        boolean exists = noticeRepository.existsByProjectAndTitle(project, request.getTitle());
        if (exists) {
            throw new CustomException(ApiResponseNoticeEnum.DUPLICATED_NOTICE);
        }

        Notice notice = new Notice(request.getTitle(), request.getDescription(), project, member);
        noticeRepository.save(notice);
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
