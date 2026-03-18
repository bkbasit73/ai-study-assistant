package com.studyassistant.controller;

import com.studyassistant.model.User;
import com.studyassistant.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/login")
public String loginPage() {
    return "login";
}

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/dashboard")
public String dashboard() {
    return "dashboard";
}
    
    @PostMapping("/register")
public String registerUser(@ModelAttribute User user) {
    user.setRole("ROLE_USER");
    userRepository.save(user);
    return "redirect:/";
}
}