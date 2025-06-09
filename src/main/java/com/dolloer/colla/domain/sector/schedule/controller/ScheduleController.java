package com.dolloer.colla.domain.sector.schedule.controller;


import com.dolloer.colla.domain.sector.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project/{projectId}/notices")
public class ScheduleController {

    private final ScheduleService scheduleService;



}
