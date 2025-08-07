package com.pro.services;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.time.Duration;

public class LLMService {

    // Holds the chat model instance used to generate answers
    private final ChatLanguageModel chatModel;

    // Constructor to initialize the chat model
    public LLMService(String modelName) {
        this.chatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(modelName)
                .timeout(Duration.ofMinutes(4))
                .logRequests(true)
                .logResponses(true)
                .build();
    }


    // Sends a prompt to the chat model and returns the generated response
    public String ask(String prompt) {
        return chatModel.generate(prompt);
    }
}