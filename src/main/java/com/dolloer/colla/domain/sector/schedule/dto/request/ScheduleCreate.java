package com.dolloer.colla.domain.sector.schedule.dto.request;

import com.dolloer.colla.domain.auth.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleCreate {

    @NotBlank(message = "제목은 필수 입니다.")
    public String title;

    public String description;

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDateTime startAt;
    @NotNull(message = "시작일은 필수입니다.")
    private LocalDateTime endAt;

    @NotBlank(message = "담당자(책임자)이메일은 필수 입니다.")
    private String email;
}
