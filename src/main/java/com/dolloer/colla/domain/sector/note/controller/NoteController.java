package com.dolloer.colla.domain.sector.note.controller;

import com.dolloer.colla.domain.sector.link.dto.request.LinkCreateRequest;
import com.dolloer.colla.domain.sector.note.dto.request.NoteCreateRequest;
import com.dolloer.colla.domain.sector.note.service.NoteService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseLinkEnum;
import com.dolloer.colla.response.response.ApiResponseNoteEnum;
import com.dolloer.colla.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/{projectId}/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    // 노트 생성
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createNote(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestBody NoteCreateRequest noteCreateRequest
    ){
        noteService.createNote(authUser.getMember(), projectId, noteCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseNoteEnum.NOTE_CREATE_SUCCESS.getMessage()));
    }
}
