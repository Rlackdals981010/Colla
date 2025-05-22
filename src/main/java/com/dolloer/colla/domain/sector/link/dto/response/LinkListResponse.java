package com.dolloer.colla.domain.sector.link.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LinkListResponse {
    List<LinkResponse> linkListResponses;
}
