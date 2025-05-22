package com.dolloer.colla.domain.sector.link.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LinkResponse {
    private Long id;
    private String title;
    private String description;
    private String url;
    private String Uploader;
    private LocalDate createAt;
}
