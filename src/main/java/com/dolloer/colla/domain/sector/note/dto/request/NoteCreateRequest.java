package com.dolloer.colla.domain.sector.note.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NoteCreateRequest {

    @NotBlank(message = "제목은 필수 입니다.")
    public String title;

    @NotBlank(message = "내용은 필수 입니다.")
    public String text;

}
