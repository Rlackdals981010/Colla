package com.dolloer.colla.domain.sector.note.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NoteResponse {
    private Long id;
    private String title;
    private String Uploader;
    private LocalDate createAt;
    private LocalDate updateAt;
}
