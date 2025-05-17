package com.dolloer.colla.domain.project.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class InviteMembersRequest {
    private List<Long> memberIds;
}