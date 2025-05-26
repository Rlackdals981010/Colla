package com.dolloer.colla.domain.sector.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NoticeCreateRequest {
    @NotBlank(message = "제목은 필수 입니다.")
    public String title;

    public String description;
}
