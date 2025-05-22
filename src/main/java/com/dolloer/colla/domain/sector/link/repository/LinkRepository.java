package com.dolloer.colla.domain.sector.link.repository;

import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.sector.link.entity.Link;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link,Long> {
    boolean existsByProjectAndUrl(Project project, @NotBlank(message = "링크는 필수 입니다.") String url);

    List<Link> findAllByProject(Project project);
}
