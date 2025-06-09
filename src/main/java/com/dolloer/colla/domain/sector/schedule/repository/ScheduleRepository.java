package com.dolloer.colla.domain.sector.schedule.repository;

import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.sector.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> findAllByProject(Project project);

    @Query("SELECT s FROM Schedule s WHERE s.project = :project AND s.startAt <= :endOfDay AND s.endAt >= :startOfDay")
    List<Schedule> findSchedulesWithinDate(
            @Param("project") Project project,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );


}
