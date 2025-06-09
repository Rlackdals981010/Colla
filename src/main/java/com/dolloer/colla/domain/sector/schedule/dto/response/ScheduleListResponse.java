package com.dolloer.colla.domain.sector.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ScheduleListResponse {
    List<ScheduleResponse> scheduleResponses;
}
