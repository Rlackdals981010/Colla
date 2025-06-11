package com.dolloer.colla.domain.sector.schedule.service;

import com.dolloer.colla.Validator.ClassValidator;
import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.auth.repository.AuthRepository;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.sector.schedule.dto.request.ProcessRequest;
import com.dolloer.colla.domain.sector.schedule.dto.request.ScheduleCreate;
import com.dolloer.colla.domain.sector.schedule.dto.request.ScheduleUpdate;
import com.dolloer.colla.domain.sector.schedule.dto.response.ScheduleListResponse;
import com.dolloer.colla.domain.sector.schedule.dto.response.ScheduleResponse;
import com.dolloer.colla.domain.sector.schedule.entity.Schedule;
import com.dolloer.colla.domain.sector.schedule.repository.ScheduleRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import com.dolloer.colla.response.response.ApiResponseScheduleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ClassValidator classValidator;
    private final AuthRepository authRepository;


    // 담당자 찾기
    private Member checkManager(String email, Project project){

        Member member = authRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ApiResponseAuthEnum.MEMBER_NOT_EXIST));

        classValidator.checkRelation(project, member);
        return member;
    }

    @Transactional
    public void createSchedule(Member member, Long projectId, ScheduleCreate scheduleCreate) {

        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

        if (scheduleCreate.getStartAt().isAfter(scheduleCreate.getEndAt()) ||
                scheduleCreate.getStartAt().isEqual(scheduleCreate.getEndAt())) {
            throw new CustomException(ApiResponseScheduleEnum.INVALID_SCHEDULE_DATE);
        }

        Member manager = checkManager(scheduleCreate.getEmail(),project);


        Schedule newSchedule = new Schedule(
                scheduleCreate.title,
                scheduleCreate.description,
                scheduleCreate.getStartAt(),
                scheduleCreate.getEndAt(),
                project,
                manager
        );

        scheduleRepository.save(newSchedule);
    }

    // 전체 조회
    public ScheduleListResponse getScheduleList(Member member, Long projectId) {
        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

        List<Schedule> schedules= scheduleRepository.findAllByProject(project);

        List<ScheduleResponse> scheduleList = schedules.stream()
                .map( schedule -> new ScheduleResponse(
                        schedule.getTitle(),
                        schedule.getDescription(),
                        schedule.getStartAt(),
                        schedule.getEndAt(),
                        schedule.getManager().getUsername()
                        )
                )
                .toList();
        return new ScheduleListResponse(scheduleList);
    }

    public ScheduleListResponse getScheduleListAt(Member member, Long projectId, LocalDate date) {
        Project project = classValidator.checkProject(projectId);
        classValidator.checkRelation(project, member);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59.999...

        List<Schedule> schedules= scheduleRepository.findSchedulesWithinDate(project,startOfDay,endOfDay);

        List<ScheduleResponse> scheduleList = schedules.stream()
                .map( schedule -> new ScheduleResponse(
                                schedule.getTitle(),
                                schedule.getDescription(),
                                schedule.getStartAt(),
                                schedule.getEndAt(),
                                schedule.getManager().getUsername()
                        )
                )
                .toList();
        return new ScheduleListResponse(scheduleList);
    }

    @Transactional
    public void updateSchedule(Member member, Long projectId, Long scheduleId, ScheduleUpdate scheduleUpdate) {
        Project project = classValidator.checkProject(projectId);
        ProjectMember projectMember = classValidator.checkRelation(project, member);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ApiResponseScheduleEnum.SCHEDULE_NOT_EXIST));

        if (!schedule.getProject().getId().equals(project.getId())) {
            throw new CustomException(ApiResponseScheduleEnum.SCHEDULE_PROJECT_DOESNT_MATCH);
        }

        boolean isManager = schedule.getManager().getId().equals(member.getId());
        boolean isPrivileged = projectMember.getRole() == ProjectRole.ADMIN || projectMember.getRole() == ProjectRole.OWNER;

        if (!isManager && !isPrivileged) {
            throw new CustomException(ApiResponseScheduleEnum.NOT_SCHEDULE_MANAGER);
        }

        if (scheduleUpdate.getTitle() != null) {
            schedule.updateTitle(scheduleUpdate.getTitle());
        }

        if (scheduleUpdate.getDescription() != null) {
            schedule.updateDescription(scheduleUpdate.getDescription());
        }

        LocalDateTime newStartAt = scheduleUpdate.getStartAt() != null ? scheduleUpdate.getStartAt() : schedule.getStartAt();
        LocalDateTime newEndAt = scheduleUpdate.getEndAt() != null ? scheduleUpdate.getEndAt() : schedule.getEndAt();

        if (newStartAt.isAfter(newEndAt) || newStartAt.isEqual(newEndAt)) {
            throw new CustomException(ApiResponseScheduleEnum.INVALID_SCHEDULE_DATE);
        }

        if (scheduleUpdate.getStartAt() != null) {
            schedule.updateStartAt(scheduleUpdate.getStartAt());
        }
        if (scheduleUpdate.getEndAt() != null) {
            schedule.updateEndAt(scheduleUpdate.getEndAt());
        }

        if (scheduleUpdate.getEmail() != null) {
            schedule.updateManager(checkManager(scheduleUpdate.getEmail(), project));
        }
    }

    @Transactional
    public void updateProcess(Member member, Long projectId, Long scheduleId, ProcessRequest processRequest) {
        Project project = classValidator.checkProject(projectId);
        ProjectMember projectMember = classValidator.checkRelation(project, member);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ApiResponseScheduleEnum.SCHEDULE_NOT_EXIST));

        if (!schedule.getProject().equals(project)) {
            throw new CustomException(ApiResponseScheduleEnum.SCHEDULE_PROJECT_DOESNT_MATCH);
        }

        boolean isManager = schedule.getManager().getId().equals(member.getId());

        if (!isManager) {
            throw new CustomException(ApiResponseScheduleEnum.NOT_SCHEDULE_MANAGER);
        }
        schedule.updatePercent(processRequest.getProcess());
    }

    @Transactional
    public void deleteSchedule(Member member, Long projectId, Long scheduleId) {
        Project project = classValidator.checkProject(projectId);
        ProjectMember projectMember = classValidator.checkRelation(project, member);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ApiResponseScheduleEnum.SCHEDULE_NOT_EXIST));

        if (!schedule.getProject().getId().equals(project.getId())) {
            throw new CustomException(ApiResponseScheduleEnum.SCHEDULE_PROJECT_DOESNT_MATCH);
        }

        boolean isManager = schedule.getManager().getId().equals(member.getId());
        boolean isPrivileged = projectMember.getRole() == ProjectRole.ADMIN || projectMember.getRole() == ProjectRole.OWNER;

        if (!isManager && !isPrivileged) {
            throw new CustomException(ApiResponseScheduleEnum.NOT_SCHEDULE_MANAGER);
        }

        scheduleRepository.delete(schedule);
    }
}
