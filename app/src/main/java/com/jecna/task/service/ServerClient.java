package com.jecna.task.service;

import com.jecna.task.model.LoginDTO;
import com.jecna.task.model.RegisterDTO;
import com.jecna.task.model.TaskDTO;

public interface ServerClient {
    void Login(LoginDTO loginDTO);
    void Register(RegisterDTO registerDTO);
    void ReadTask();
    void SaveTask(TaskDTO[] taskDTOS);
}
