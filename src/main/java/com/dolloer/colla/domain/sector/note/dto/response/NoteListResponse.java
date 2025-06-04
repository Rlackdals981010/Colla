package com.dolloer.colla.domain.sector.note.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NoteListResponse {
    List<NoteResponse> noteResponseList;
}
