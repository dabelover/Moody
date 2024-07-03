package com.example.moody;

public class GeminiRequest {
    private String prompt;

    public GeminiRequest(String prompt) {
        this.prompt = prompt;
    }

    // Getter y Setter
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}