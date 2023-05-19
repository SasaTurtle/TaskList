package com.jecna.task.model;

public class RegisterDTO {
    String username;
    String email;
    String[] role;
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getRole() {
        return role;
    }

    public void setRole(String[] role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterDTO(String username, String email, String[] role, String password) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }
}
