package com.studyassistant.controller;

import com.studyassistant.model.AIChat;
import com.studyassistant.model.Note;
import com.studyassistant.model.User;
import com.studyassistant.repository.AIChatRepository;
import com.studyassistant.repository.NoteRepository;
import com.studyassistant.repository.UserRepository;
import com.studyassistant.service.AIService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AIController {

    private AIService aiService;
    private UserRepository userRepository;
    private AIChatRepository aiChatRepository;
    private NoteRepository noteRepository;

    public AIController(AIService aiService,
                        UserRepository userRepository,
                        AIChatRepository aiChatRepository,
                        NoteRepository noteRepository) {

        this.aiService = aiService;
        this.userRepository = userRepository;
        this.aiChatRepository = aiChatRepository;
        this.noteRepository = noteRepository;
    }

    @GetMapping("/ask-ai")
    public String askAiPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<AIChat> chatHistory = aiChatRepository.findByUserOrderByCreatedAtDesc(user);
        model.addAttribute("chatHistory", chatHistory);

        return "ask-ai";
    }

    @PostMapping("/ask-ai")
    public String askAI(@RequestParam("question") String question,
                        Model model,
                        Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        String answer = aiService.askAI(question);

        AIChat chat = new AIChat();
        chat.setQuestion(question);
        chat.setAnswer(answer);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUser(user);

        aiChatRepository.save(chat);

        List<AIChat> chatHistory = aiChatRepository.findByUserOrderByCreatedAtDesc(user);

        model.addAttribute("answer", answer);
        model.addAttribute("question", question);
        model.addAttribute("chatHistory", chatHistory);

        return "ask-ai";
    }

    @GetMapping("/quiz")
    public String quizPage() {
        return "quiz";
    }

    @PostMapping("/quiz")
    public String generateQuiz(Model model, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Note> notes = noteRepository.findByUser(user);

        if (notes.isEmpty()) {
            model.addAttribute("quiz", "No notes found. Please add some notes first.");
            return "quiz";
        }

        StringBuilder notesText = new StringBuilder();

        for (Note note : notes) {
            notesText.append("Title: ")
                    .append(note.getTitle())
                    .append("\nContent: ")
                    .append(note.getContent())
                    .append("\n\n");
        }

        String prompt = "Create 5 short study quiz questions for a student based only on these notes.\n\n"
                + notesText
                + "\nMake the questions clear, simple, and numbered.";

        String quiz = aiService.askAI(prompt);

        model.addAttribute("quiz", quiz);

        return "quiz";
    }
}