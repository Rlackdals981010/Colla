package com.dolloer.colla.domain.sector.notice.service;

import com.dolloer.colla.Validator.ClassValidator;
import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.sector.notice.dto.request.NoticeCreateRequest;
import com.dolloer.colla.domain.sector.notice.dto.request.NoticeUpdateRequest;
import com.dolloer.colla.domain.sector.notice.dto.response.NoticeListResponse;
import com.dolloer.colla.domain.sector.notice.dto.response.NoticeResponse;
import com.dolloer.colla.domain.sector.notice.entity.Notice;
import com.dolloer.colla.domain.sector.notice.repository.NoticeRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseNoticeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final ClassValidator classValidator;


    // 생성
    @Transactional
    public void createNotice(Member member, Long projectId, NoticeCreateRequest request) {
        Project project = classValidator.checkProject(projectId);
        ProjectMember relation = classValidator.checkRelation(project, member);

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
        Project project = classValidator.checkProject(projectId);
        ProjectMember relation = classValidator.checkRelation(project, member);

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
        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

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
        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

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

    // 공지 수정
    @Transactional
    public void updateNotice(Member member, Long projectId, Long noticeId, NoticeUpdateRequest noticeUpdateRequest) {
        Project project = classValidator.checkProject(projectId);
        ProjectMember relation = classValidator.checkRelation(project, member);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ApiResponseNoticeEnum.NOTICE_NOT_EXIST));

        // 관리자만 수정 가능
        if (relation.getRole() == ProjectRole.MEMBER) {
            throw new CustomException(ApiResponseNoticeEnum.NOT_ENOUGH_PERMISSION);
        }

        if(noticeUpdateRequest.getTitle()!=null){
            notice.updateTitle(noticeUpdateRequest.getTitle());
        }
        if(noticeUpdateRequest.getDescription()!=null){
            notice.updateDescription(noticeUpdateRequest.getDescription());
        }

    }

    // 공지 삭제
    @Transactional
    public void deleteNotice(Member member, Long projectId, Long noticeId) {
        Project project = classValidator.checkProject(projectId);
        ProjectMember relation = classValidator.checkRelation(project, member);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ApiResponseNoticeEnum.NOTICE_NOT_EXIST));

        // 관리자만 삭제 가능
        if (relation.getRole() == ProjectRole.MEMBER) {
            throw new CustomException(ApiResponseNoticeEnum.NOT_ENOUGH_PERMISSION);
        }

        noticeRepository.delete(notice);
    }


}
