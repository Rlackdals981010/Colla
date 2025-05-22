package com.dolloer.colla.domain.sector.link.controller;

import com.dolloer.colla.domain.auth.dto.request.SignupRequest;
import com.dolloer.colla.domain.auth.dto.response.SignupResponse;
import com.dolloer.colla.domain.sector.link.dto.request.LinkCreateRequest;
import com.dolloer.colla.domain.sector.link.service.LinkService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseLinkEnum;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import com.dolloer.colla.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    // 링크 공유 글 생성
    @PostMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Void>> createLink(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestBody LinkCreateRequest linkCreateRequest
    ) {
        linkService.createLink(authUser.getMember(), projectId,linkCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseLinkEnum.LINK_CREATE_SUCCESS.getMessage()));
    }

}
