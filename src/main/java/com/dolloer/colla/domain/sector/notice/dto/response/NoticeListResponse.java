package com.dolloer.colla.domain.sector.notice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NoticeListResponse {
    List<NoticeResponse> noticeResponseList;
}
