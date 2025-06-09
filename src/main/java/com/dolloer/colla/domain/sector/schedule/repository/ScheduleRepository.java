package com.dolloer.colla.domain.sector.schedule.repository;

import com.dolloer.colla.domain.sector.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
