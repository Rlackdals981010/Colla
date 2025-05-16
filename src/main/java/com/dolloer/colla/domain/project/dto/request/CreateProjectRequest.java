package com.dolloer.colla.domain.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateProjectRequest {

    @NotBlank(message = "프로젝트 이름은 필수입니다.")
    private String name;

    private String description;

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "마감일은 필수입니다.")
    private LocalDate endDate;
}