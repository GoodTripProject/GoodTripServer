package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note, Integer> {

    Integer deleteNoteById(Integer noteId);


    Optional<Note> getNoteById(int noteId);
}
