package com.jecna.task.service;

//Data interface

import com.jecna.task.model.TaskModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface DataService {
    List<TaskModel> read() throws IOException, ClassNotFoundException;

    UUID create(TaskModel task) throws IOException, ClassNotFoundException;

    boolean update (TaskModel task) throws IOException, ClassNotFoundException;

    boolean delete (UUID taskId) throws IOException, ClassNotFoundException;

    boolean restore (TaskModel task) throws IOException, ClassNotFoundException;
}
