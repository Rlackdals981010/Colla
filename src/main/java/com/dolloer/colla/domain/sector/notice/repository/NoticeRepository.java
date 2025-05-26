package com.dolloer.colla.domain.sector.notice.repository;

import com.dolloer.colla.domain.sector.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

}
