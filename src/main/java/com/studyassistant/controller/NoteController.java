package com.studyassistant.controller;

import com.studyassistant.model.Note;
import com.studyassistant.model.User;
import com.studyassistant.repository.NoteRepository;
import com.studyassistant.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NoteController {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteController(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/notes")
    public String showNotes(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Note> notes = noteRepository.findByUser(user);
        model.addAttribute("notes", notes);

        return "notes";
    }

    @PostMapping("/notes")
    public String saveNote(@ModelAttribute Note note, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        note.setUser(user);
        noteRepository.save(note);

        return "redirect:/notes";
    }
}