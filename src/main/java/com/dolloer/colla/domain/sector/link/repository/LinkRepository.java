package com.dolloer.colla.domain.sector.link.repository;

import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.sector.link.entity.Link;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link,Long> {
    boolean existsByProjectAndUrl(Project project, @NotBlank(message = "링크는 필수 입니다.") String url);

    List<Link> findAllByProject(Project project);

    @Query("SELECT l FROM Link l WHERE l.project = :project AND LOWER(l.linkTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Link> searchByTitle(@Param("project") Project project, @Param("keyword") String keyword);
    // List<Link> searchByTitleNative(Project project, String keyword);
}
