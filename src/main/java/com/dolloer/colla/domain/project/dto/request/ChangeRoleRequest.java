package com.dolloer.colla.domain.project.dto.request;

import com.dolloer.colla.domain.project.entity.ProjectRole;
import lombok.Getter;

@Getter
public class ChangeRoleRequest {
    public ProjectRole role;
}
