package com.dolloer.colla.domain.sector.notice.controller;


import com.dolloer.colla.domain.sector.notice.dto.request.NoticeCreateRequest;
import com.dolloer.colla.domain.sector.notice.dto.request.NoticeUpdateRequest;
import com.dolloer.colla.domain.sector.notice.dto.response.NoticeListResponse;
import com.dolloer.colla.domain.sector.notice.dto.response.NoticeResponse;
import com.dolloer.colla.domain.sector.notice.service.NoticeService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseLinkEnum;
import com.dolloer.colla.response.response.ApiResponseNoticeEnum;
import com.dolloer.colla.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/{projectId}/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지 목록 조회
    @GetMapping()
    public ResponseEntity<ApiResponse<NoticeListResponse>> getNoticeList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        NoticeListResponse result = noticeService.getNoticeList(authUser.getMember(),projectId);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseNoticeEnum.NOTICE_LIST_READ_SUCCESS.getMessage()));
    }

    // 공지 이름 기반 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<NoticeListResponse>> searchNoticesByTitle(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestParam String keyword
    ){
        NoticeListResponse result = noticeService.searchNoticesByTitle(authUser.getMember(),projectId,keyword);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseNoticeEnum.NOTICE_LIST_SEARCH_SUCCESS.getMessage()));

    }
    // 공지 단건 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNotice(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long noticeId
    ){
        NoticeResponse result = noticeService.getNotice(authUser.getMember(),projectId,noticeId);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseNoticeEnum.NOTICE_READ_SUCCESS.getMessage()));
    }

    // 공지 생성
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createNotice(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestBody NoticeCreateRequest noticeCreateRequest
    ){
        noticeService.createNotice(authUser.getMember(),projectId,noticeCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseNoticeEnum.NOTICE_CREATE_SUCCESS.getMessage()));
    }

    // 공지 수정
    @PatchMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> updateNotice(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long noticeId,
            @RequestBody NoticeUpdateRequest noticeUpdateRequest
    ){
        noticeService.updateNotice(authUser.getMember(), projectId, noticeId,noticeUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseNoticeEnum.NOTICE_UPDATE_SUCCESS.getMessage()));
    }

    // 공지 삭제
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long noticeId
    ){
        noticeService.deleteNotice(authUser.getMember(), projectId, noticeId);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseNoticeEnum.NOTICE_DELETE_SUCCESS.getMessage()));
    }

}
