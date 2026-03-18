package com.studyassistant.repository;

import com.studyassistant.model.Note;
import com.studyassistant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUser(User user);

}