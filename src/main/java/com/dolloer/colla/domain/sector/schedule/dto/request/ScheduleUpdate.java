package com.dolloer.colla.domain.sector.schedule.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleUpdate {

    public String title;

    public String description;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String email;
}
