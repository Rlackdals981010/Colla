package com.dolloer.colla.domain.sector.schedule.controller;


import com.dolloer.colla.domain.sector.schedule.dto.request.ScheduleCreate;
import com.dolloer.colla.domain.sector.schedule.dto.request.ScheduleUpdate;
import com.dolloer.colla.domain.sector.schedule.dto.response.ScheduleListResponse;
import com.dolloer.colla.domain.sector.schedule.service.ScheduleService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseScheduleEnum;
import com.dolloer.colla.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project/{projectId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 생성
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createSchedule(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestBody ScheduleCreate scheduleCreate
            ){
        scheduleService.createSchedule(authUser.getMember(),projectId,scheduleCreate);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseScheduleEnum.SCHEDULE_CREATE_SUCCESS.getMessage()));
    }

    // 전체 조회
    @GetMapping()
    public ResponseEntity<ApiResponse<ScheduleListResponse>> getScheduleList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId
    ){
        ScheduleListResponse result = scheduleService.getScheduleList(authUser.getMember(),projectId);
        return ResponseEntity.ok(ApiResponse.success(result,ApiResponseScheduleEnum.SCHEDULE_GET_LIST_SUCCESS.getMessage()));
    }

    // 특정 날 일정 조회
    @GetMapping("/at")
    public ResponseEntity<ApiResponse<ScheduleListResponse>> getScheduleListAt(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @RequestParam LocalDate date
    ){
        ScheduleListResponse result = scheduleService.getScheduleListAt(authUser.getMember(),projectId,date);
        return ResponseEntity.ok(ApiResponse.success(result,ApiResponseScheduleEnum.SCHEDULE_GET_LIST_AT_SUCCESS.getMessage()));
    }

    // 일정 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<ScheduleListResponse>> updateSchedule(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long projectId,
            @PathVariable Long scheduleId,
            @RequestBody ScheduleUpdate scheduleUpdate
            ){
        scheduleService.updateSchedule(authUser.getMember(),projectId,scheduleId,scheduleUpdate);
        return ResponseEntity.ok(ApiResponse.success(ApiResponseScheduleEnum.SCHEDULE_UPDATE_SUCCESS.getMessage()));
    }
    // 일정 진행률 업데이트
    // 삭제



}
