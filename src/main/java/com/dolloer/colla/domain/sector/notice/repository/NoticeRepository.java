package com.dolloer.colla.domain.sector.notice.repository;

import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.sector.notice.entity.Notice;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

    boolean existsByProjectAndTitle(Project project, @NotBlank(message = "제목은 필수 입니다.") String title);
}
