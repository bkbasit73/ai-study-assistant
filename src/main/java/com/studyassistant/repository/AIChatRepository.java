package com.studyassistant.repository;

import com.studyassistant.model.AIChat;
import com.studyassistant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIChatRepository extends JpaRepository<AIChat, Long> {
    List<AIChat> findByUserOrderByCreatedAtDesc(User user);
}