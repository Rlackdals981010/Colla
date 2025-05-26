package com.dolloer.colla.domain.sector.notice.service;

import com.dolloer.colla.domain.sector.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
}
