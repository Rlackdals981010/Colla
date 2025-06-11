package com.dolloer.colla.domain.sector.notice.repository;

import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.sector.notice.entity.Notice;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

    boolean existsByProjectAndTitle(Project project, @NotBlank(message = "제목은 필수 입니다.") String title);

    List<Notice> findAllByProject(Project project);

    @Query("SELECT n FROM Notice n WHERE n.project = :project AND LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Notice> searchByTitle(@Param("project") Project project, @Param("keyword") String keyword);
}
