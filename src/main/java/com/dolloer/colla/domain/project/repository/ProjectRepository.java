package com.dolloer.colla.domain.project.repository;

import com.dolloer.colla.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
