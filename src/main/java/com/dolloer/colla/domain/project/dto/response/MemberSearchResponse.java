
package com.dolloer.colla.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MemberSearchResponse {
    private Long id;
    private String username;
    private String email;
}