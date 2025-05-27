package com.dolloer.colla.domain.sector.notice.service;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.domain.sector.notice.dto.request.NoticeCreateRequest;
import com.dolloer.colla.domain.sector.notice.dto.response.NoticeListResponse;
import com.dolloer.colla.domain.sector.notice.dto.response.NoticeResponse;
import com.dolloer.colla.domain.sector.notice.entity.Notice;
import com.dolloer.colla.domain.sector.notice.repository.NoticeRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseNoticeEnum;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    // 생성
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

    // 전체 조회
    public NoticeListResponse getNoticeList(Member member, Long projectId) {
        Project project = checkProject(projectId);
        ProjectMember relation = checkRelation(project, member);

        List<Notice> notices = noticeRepository.findAllByProject(project);

        List<NoticeResponse> noticeList = notices.stream()
                .map(notice -> new NoticeResponse(
                                notice.getId(),
                                notice.getTitle(),
                                notice.getDescription(),
                                notice.getUploader().getUsername(),
                                notice.getCreatedAt().toLocalDate(),
                                notice.getUpdatedAt().toLocalDate()
                        )
                ).toList();
        return new NoticeListResponse(noticeList);
    }

    // 검색 조회
    public NoticeListResponse searchNoticesByTitle(Member member, Long projectId, String keyword) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        List<Notice> notices = noticeRepository.searchByTitle(project, keyword);

        List<NoticeResponse> noticeList = notices.stream()
                .map(notice -> new NoticeResponse(
                                notice.getId(),
                                notice.getTitle(),
                                notice.getDescription(),
                                notice.getUploader().getUsername(),
                                notice.getCreatedAt().toLocalDate(),
                                notice.getUpdatedAt().toLocalDate()
                        )
                ).toList();
        return new NoticeListResponse(noticeList);
    }

    // 단건 조회
    public NoticeResponse getNotice(Member member, Long projectId, Long noticeId) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ApiResponseNoticeEnum.NOTICE_NOT_EXIST));

        if (!notice.getProject().equals(project)) {
            throw new CustomException(ApiResponseNoticeEnum.NOT_THIS_PROJECT_NOTICE);
        }

        return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getDescription(),
                notice.getUploader().getUsername(),
                notice.getCreatedAt().toLocalDate(),
                notice.getUpdatedAt().toLocalDate()
        );
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
