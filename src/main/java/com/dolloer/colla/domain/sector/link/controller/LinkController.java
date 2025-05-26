package com.dolloer.colla.domain.sector.link.controller;

import com.dolloer.colla.domain.sector.link.dto.request.LinkCreateRequest;
import com.dolloer.colla.domain.sector.link.dto.request.LinkUpdateRequest;
import com.dolloer.colla.domain.sector.link.dto.response.LinkListResponse;
import com.dolloer.colla.domain.sector.link.dto.response.LinkResponse;
import com.dolloer.colla.domain.sector.link.service.LinkService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseLinkEnum;
import com.dolloer.colla.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/{projectId}/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    // 링크 공유 글 생성
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createLink(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestBody LinkCreateRequest linkCreateRequest
    ) {
        linkService.createLink(authUser.getMember(), projectId,linkCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseLinkEnum.LINK_CREATE_SUCCESS.getMessage()));
    }

    // 프로젝트 소속 링크 글 전체 보기
    @GetMapping()
    public ResponseEntity<ApiResponse<LinkListResponse>> getLinkList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        LinkListResponse result = linkService.getLinkList(authUser.getMember(), projectId);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseLinkEnum.LINK_LIST_READ_SUCCESS.getMessage()));
    }

    // 링크 글 상세 보기
    @GetMapping("/{linkId}")
    public ResponseEntity<ApiResponse<LinkResponse>> getLink(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long linkId
    ){
        LinkResponse result = linkService.getLink(authUser.getMember(),projectId, linkId);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseLinkEnum.LINK_READ_SUCCESS.getMessage()));
    }

    // 링크 글 수정하기
    @PatchMapping("/{linkId}")
    public ResponseEntity<ApiResponse<LinkResponse>> updateLink(@AuthenticationPrincipal AuthUser authUser,
                                                                @PathVariable Long projectId,
                                                                @PathVariable Long linkId,
                                                                @RequestBody LinkUpdateRequest linkUpdateRequest
                                                                ){
        LinkResponse result = linkService.updateLink(authUser.getMember(),projectId, linkId,linkUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseLinkEnum.LINK_UPDATE_SUCCESS.getMessage()));
    }

    // 링크 글 삭제하기
    @DeleteMapping("/{linkId}")
    public ResponseEntity<ApiResponse<Void>> deleteLink(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long linkId
    ){
        linkService.deleteLink(authUser.getMember(),projectId,linkId);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseLinkEnum.LINK_DELETE_SUCCESS.getMessage()));
    }

    // 링크 이름 기반 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<LinkListResponse>> searchLinksByTitle(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestParam String keyword // ex: ?keyword=검색어
    ) {
        LinkListResponse result = linkService.searchLinksByTitle(authUser.getMember(), projectId, keyword);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

}
