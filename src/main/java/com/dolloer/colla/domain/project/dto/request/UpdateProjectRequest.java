package com.dolloer.colla.domain.project.dto.request;

import jakarta.annotation.Nullable;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateProjectRequest {

    @Nullable
    private String name;
    @Nullable
    private String description;
    @Nullable
    private LocalDate startDate;
    @Nullable
    private LocalDate endDate;
}
