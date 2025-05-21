
package com.dolloer.colla.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberListResponse {
    List<ProjectMembersResponse> memberSearchResponses;
}