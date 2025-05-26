package com.dolloer.colla.domain.sector.notice.controller;

import com.dolloer.colla.domain.sector.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
}
