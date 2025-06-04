package com.dolloer.colla.domain.sector.note.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NoteDetailResponse {
    private Long id;
    private String title;
    private String text;
    private String Uploader;
    private LocalDate createAt;
    private LocalDate updateAt;
}
