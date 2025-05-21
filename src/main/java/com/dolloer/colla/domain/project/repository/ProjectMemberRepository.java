package com.dolloer.colla.domain.project.repository;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);

    List<ProjectMember> findAllByMember(Member member);

    List<ProjectMember> findAllByProject(Project project);
}
