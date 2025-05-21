
package com.dolloer.colla.domain.project.dto.response;

import com.dolloer.colla.domain.project.entity.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectMembersResponse {
    private Long id;
    private String username;
    private String email;
    private ProjectRole role;
}