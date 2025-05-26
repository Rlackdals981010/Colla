package com.dolloer.colla.domain.sector.notice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NoticeResponse {
    private Long id;
    private String title;
    private String description;
    private String Uploader;
    private LocalDate createAt;
    private LocalDate updateAt;
}
