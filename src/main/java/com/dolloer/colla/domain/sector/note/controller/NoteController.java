package com.dolloer.colla.domain.sector.note.controller;

import com.dolloer.colla.domain.sector.note.dto.request.NoteCreateRequest;
import com.dolloer.colla.domain.sector.note.dto.request.NoteUpdateRequest;
import com.dolloer.colla.domain.sector.note.dto.response.NoteDetailResponse;
import com.dolloer.colla.domain.sector.note.dto.response.NoteListResponse;
import com.dolloer.colla.domain.sector.note.service.NoteService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseNoteEnum;
import com.dolloer.colla.security.AuthUser;
import jakarta.validation.Valid;
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
            @Valid @RequestBody NoteCreateRequest noteCreateRequest
    ){
        noteService.createNote(authUser.getMember(), projectId, noteCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseNoteEnum.NOTE_CREATE_SUCCESS.getMessage()));
    }

    // 노트 리스트 조회
    @GetMapping()
    public ResponseEntity<ApiResponse<NoteListResponse>> getNoteList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        NoteListResponse result = noteService.getNoteList(authUser.getMember(), projectId);
        return ResponseEntity.ok(ApiResponse.success(result,ApiResponseNoteEnum.NOTE_LIST_GET_SUCCESS.getMessage()));
    }

    // 노트 단건 조회
    @GetMapping("/{noteId}")
    public ResponseEntity<ApiResponse<NoteDetailResponse>> getNote(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long noteId
    ){
        NoteDetailResponse result = noteService.getNote(authUser.getMember(), projectId,noteId);
        return ResponseEntity.ok(ApiResponse.success(result,ApiResponseNoteEnum.NOTE_GET_SUCCESS.getMessage()));
    }

    // 노트 검색 조회
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<NoteListResponse>> searchNoteByTitle(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestParam String keyword
    ){
        NoteListResponse result = noteService.searchNoteByTitle(authUser.getMember(), projectId, keyword);
        return ResponseEntity.ok(ApiResponse.success(result,ApiResponseNoteEnum.NOTE_SEARCH_SUCCESS.getMessage()));
    }

    // 노트 수정
    @PatchMapping("/{noteId}")
    public ResponseEntity<ApiResponse<Void>> updateNote(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long noteId,
            @Valid @RequestBody NoteUpdateRequest noteUpdateRequest
    ){
        noteService.updateNote(authUser.getMember(),projectId,noteId,noteUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseNoteEnum.NOTE_UPDATE_SUCCESS.getMessage()));
    }

    // 노트 삭제
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long noteId
    ){
        noteService.deleteNote(authUser.getMember(),projectId,noteId);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseNoteEnum.NOTE_DELETE_SUCCESS.getMessage()));
    }


}
