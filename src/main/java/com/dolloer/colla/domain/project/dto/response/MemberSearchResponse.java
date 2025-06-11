
package com.dolloer.colla.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSearchResponse {
    private Long id;
    private String username;
    private String email;
}