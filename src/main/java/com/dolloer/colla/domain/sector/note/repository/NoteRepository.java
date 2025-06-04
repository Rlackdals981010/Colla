package com.dolloer.colla.domain.sector.note.repository;

import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.sector.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {

    List<Note> findAllByProject(Project project);
}
