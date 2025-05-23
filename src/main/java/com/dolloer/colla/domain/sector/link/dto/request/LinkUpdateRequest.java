package com.dolloer.colla.domain.sector.link.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LinkUpdateRequest {


    @Nullable
    public String title;
    @Nullable
    public String description;
    @Nullable
    public String url;
}
