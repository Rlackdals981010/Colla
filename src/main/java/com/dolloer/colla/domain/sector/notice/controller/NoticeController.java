package com.dolloer.colla.domain.sector.notice.controller;


import com.dolloer.colla.domain.sector.notice.dto.request.NoticeCreateRequest;
import com.dolloer.colla.domain.sector.notice.service.NoticeService;
import com.dolloer.colla.response.response.ApiResponse;
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
    // 공지 이름 기반 검색
    // 공지 단건 조회

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
    // 공지 삭제
}
