package com.jecna.task.model;

public class RegisterResponseDTO {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RegisterResponseDTO(String message) {
        this.message = message;
    }
}
