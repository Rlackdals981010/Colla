package com.dolloer.colla.domain.sector.link.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LinkCreateRequest {

    @NotBlank(message = "제목은 필수 입니다.")
    public String title;

    public String description;

    @NotBlank(message = "링크는 필수 입니다.")
    public String url;
}
