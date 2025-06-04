package com.dolloer.colla.domain.sector.note.repository;

import com.dolloer.colla.domain.sector.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Long> {

}
