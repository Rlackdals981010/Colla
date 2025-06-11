package com.dolloer.colla.domain.sector.note.service;

import com.dolloer.colla.Validator.ClassValidator;
import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.sector.note.dto.request.NoteCreateRequest;
import com.dolloer.colla.domain.sector.note.dto.request.NoteUpdateRequest;
import com.dolloer.colla.domain.sector.note.dto.response.NoteDetailResponse;
import com.dolloer.colla.domain.sector.note.dto.response.NoteListResponse;
import com.dolloer.colla.domain.sector.note.dto.response.NoteResponse;
import com.dolloer.colla.domain.sector.note.entity.Note;
import com.dolloer.colla.domain.sector.note.repository.NoteRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseNoteEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final ClassValidator classValidator;

    @Transactional
    public void createNote(Member member, Long projectId, NoteCreateRequest noteCreateRequest) {
        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

        Note newNote = new Note(noteCreateRequest.getTitle(), noteCreateRequest.getText(), member, project);
        noteRepository.save(newNote);
    }

    // 리스트 조회
    public NoteListResponse getNoteList(Member member, Long projectId) {
        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

        List<Note> notes = noteRepository.findAllByProject(project);

        List<NoteResponse> noteList = notes.stream()
                .map(note -> new NoteResponse(
                        note.getId(),
                        note.getTitle(),
                        note.getUploader().getUsername(),
                        note.getCreatedAt().toLocalDate(),
                        note.getUpdatedAt().toLocalDate()
                ))
                .toList();

        return new NoteListResponse(noteList);
    }

    // 단건 디테일 조회
    public NoteDetailResponse getNote(Member member, Long projectId, Long noteId) {
        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new CustomException(ApiResponseNoteEnum.NOTE_NOT_EXIST));

        if (!note.getProject().equals(project)) {
            throw new CustomException(ApiResponseNoteEnum.NOT_THIS_PROJECT_NOTICE);
        }

        return new NoteDetailResponse(
                note.getId(),
                note.getTitle(),
                note.getText(),
                note.getUploader().getUsername(),
                note.getCreatedAt().toLocalDate(),
                note.getUpdatedAt().toLocalDate()
        );
    }

    // 검색
    public NoteListResponse searchNoteByTitle(Member member, Long projectId, String keyword) {
        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

        List<Note> notes = noteRepository.searchByTitle(project, keyword);

        List<NoteResponse> noteList = notes.stream()
                .map(note -> new NoteResponse(
                        note.getId(),
                        note.getTitle(),
                        note.getUploader().getUsername(),
                        note.getCreatedAt().toLocalDate(),
                        note.getUpdatedAt().toLocalDate()
                ))
                .toList();

        return new NoteListResponse(noteList);
    }

    // 노트 수정
    @Transactional
    public void updateNote(Member member, Long projectId, Long noteId, NoteUpdateRequest noteUpdateRequest) {
        Project project = classValidator.checkProject(projectId);
        ProjectMember projectMember = classValidator.checkRelation(project, member);

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new CustomException(ApiResponseNoteEnum.NOTE_NOT_EXIST));

        boolean isUploader = member.getId().equals(note.getUploader().getId());
        boolean isManager = projectMember.getRole() == ProjectRole.OWNER || projectMember.getRole() == ProjectRole.ADMIN;

        if (!isUploader && !isManager) {
            throw new CustomException(ApiResponseNoteEnum.NOT_ENOUGH_PERMISSION);
        }

        if(noteUpdateRequest.getText()!=null&&!noteUpdateRequest.getText().isBlank()){
            note.updateText(noteUpdateRequest.getText());
        }
        if(noteUpdateRequest.getTitle()!=null&&!noteUpdateRequest.getTitle().isBlank()){
            note.updateTitle(noteUpdateRequest.getTitle());
        }
    }

    // 노트 삭제
    @Transactional
    public void deleteNote(Member member, Long projectId, Long noteId) {
        Project project = classValidator.checkProject(projectId);
        ProjectMember projectMember = classValidator.checkRelation(project, member);

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new CustomException(ApiResponseNoteEnum.NOTE_NOT_EXIST));

        boolean isUploader = member.getId().equals(note.getUploader().getId());
        boolean isManager = projectMember.getRole() == ProjectRole.OWNER || projectMember.getRole() == ProjectRole.ADMIN;

        if (!isUploader && !isManager) {
            throw new CustomException(ApiResponseNoteEnum.NOT_ENOUGH_PERMISSION);
        }

        noteRepository.delete(note);

    }


}
