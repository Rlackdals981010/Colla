package com.dolloer.colla.domain.sector.schedule.service;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.auth.repository.AuthRepository;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.domain.sector.schedule.dto.request.ScheduleCreate;
import com.dolloer.colla.domain.sector.schedule.dto.response.ScheduleListResponse;
import com.dolloer.colla.domain.sector.schedule.dto.response.ScheduleResponse;
import com.dolloer.colla.domain.sector.schedule.entity.Schedule;
import com.dolloer.colla.domain.sector.schedule.repository.ScheduleRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import com.dolloer.colla.response.response.ApiResponseScheduleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthRepository authRepository;

    // 프로젝트 존재 확인
    private Project checkProject(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));
    }

    // 유저가 프로젝트에 속한지 확인
    private ProjectMember checkRelation(Project project, Member member ){
        return projectMemberRepository.findByProjectAndMember(project, member)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_THIS_PROJECT_MEMBER));
    }

    // 담당자 찾기
    private Member checkMember(String email, Project project){

        Member member = authRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ApiResponseAuthEnum.MEMBER_NOT_EXIST));

        checkRelation(project, member);
        return member;
    }

    @Transactional
    public void createSchedule(Member member, Long projectId, ScheduleCreate scheduleCreate) {

        Project project = checkProject(projectId);
        checkRelation(project, member);

        if (scheduleCreate.getStartAt().isAfter(scheduleCreate.getEndAt()) ||
                scheduleCreate.getStartAt().isEqual(scheduleCreate.getEndAt())) {
            throw new CustomException(ApiResponseScheduleEnum.INVALID_SCHEDULE_DATE);
        }

        Member manager = checkMember(scheduleCreate.getEmail(),project);


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
        Project project = checkProject(projectId);
        checkRelation(project, member);

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
}
