package com.dolloer.colla.domain.sector.note.repository;

import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.sector.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {

    List<Note> findAllByProject(Project project);

    @Query("SELECT n FROM Note n WHERE n.project = :project AND LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Note> searchByTitle(Project project, String keyword);
}
