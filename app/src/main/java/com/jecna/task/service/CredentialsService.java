package com.jecna.task.service;

import com.jecna.task.model.LoginDTO;

public interface CredentialsService {
    LoginDTO ReadFile();
    void WriteFile(LoginDTO loginDTO);
}
