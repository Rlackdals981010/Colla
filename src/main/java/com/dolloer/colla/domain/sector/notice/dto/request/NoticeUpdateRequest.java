package com.dolloer.colla.domain.sector.notice.dto.request;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class NoticeUpdateRequest {

    @Nullable
    public String title;
    @Nullable
    public String description;
}
