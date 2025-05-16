package com.dolloer.colla.domain.project.service;

import com.dolloer.colla.domain.project.dto.request.CreateProjectRequest;
import com.dolloer.colla.domain.project.dto.response.ProjectResponse;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponse createProject(CreateProjectRequest createProjectRequest) {
        Project project = new Project(createProjectRequest.getName(),createProjectRequest.getDescription(),createProjectRequest.getStartDate(),createProjectRequest.getEndDate());
        projectRepository.save(project);
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getStartDate(), project.getEndDate());
    }
}
