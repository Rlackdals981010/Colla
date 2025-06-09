package com.dolloer.colla.domain.sector.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponse {

    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String managerName;

}
