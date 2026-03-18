package com.studyassistant.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    public String askAI(String question) {
        try {
            String lowerQuestion = question.toLowerCase();

            // Handle real-time questions directly in Java
            if (lowerQuestion.contains("time")) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                return "The current server time is " + now.format(formatter) + ".";
            }

            String url = "http://localhost:11434/api/generate";

            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama3.1:8b");
            requestBody.put("prompt", "Answer briefly and clearly for a student: " + question);
            requestBody.put("stream", false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response =
                    (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate.postForEntity(
                            url, entity, Map.class
                    );

            if (response.getBody() != null && response.getBody().get("response") != null) {
                return response.getBody().get("response").toString();
            } else {
                return "No response received from AI.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error while connecting to AI: " + e.getMessage();
        }
    }
}