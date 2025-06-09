package com.dolloer.colla.domain.sector.schedule.controller;


import com.dolloer.colla.domain.sector.schedule.dto.request.ScheduleCreate;
import com.dolloer.colla.domain.sector.schedule.service.ScheduleService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseScheduleEnum;
import com.dolloer.colla.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project/{projectId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createSchedule(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestBody ScheduleCreate scheduleCreate
            ){
        scheduleService.createSchedule(authUser.getMember(),projectId,scheduleCreate);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseScheduleEnum.SCHEDULE_CREATE_SUCCESS.getMessage()));
    }

}
