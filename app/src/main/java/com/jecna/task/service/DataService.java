package com.jecna.task.service;



import com.jecna.task.model.TaskModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface DataService {
    List<TaskModel> read() throws IOException, ClassNotFoundException;

    int create(TaskModel task) throws IOException, ClassNotFoundException;

    boolean update (TaskModel task) throws IOException, ClassNotFoundException;

    boolean delete (int taskId) throws IOException, ClassNotFoundException;

    boolean restore (TaskModel task) throws IOException, ClassNotFoundException;
}
