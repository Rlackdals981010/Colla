package com.dolloer.colla.domain.project.controller;

import com.dolloer.colla.domain.auth.dto.response.SignupResponse;
import com.dolloer.colla.domain.project.dto.request.CreateProjectRequest;
import com.dolloer.colla.domain.project.dto.response.ProjectResponse;
import com.dolloer.colla.domain.project.service.ProjectService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping()
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@Valid  @RequestBody CreateProjectRequest createProjectRequest) {
        ProjectResponse projectResponse = projectService.createProject(createProjectRequest);
        return ResponseEntity.ok(ApiResponse.success(projectResponse, ApiResponseProjectEnum.PROJECT_CREATE_SUCCESS.getMessage()));
    }

}
