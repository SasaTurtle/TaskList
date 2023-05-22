package com.jecna.task.service;


import android.app.Activity;
import android.content.Context;
import com.jecna.task.model.TaskModel;


import java.io.*;
import java.util.*;

public class DataServiceImpl implements DataService {
    private Activity owner;

    public DataServiceImpl(Activity owner) {
        this.owner = owner;
    }

    @Override
    public List<TaskModel> read() throws IOException, ClassNotFoundException {
        return readSerialData();
    }

    /***
     * Adds a new task to the task list with a new id
     * @param task
     * @return new task ID
     * @throws IOException
     */
    @Override
    public UUID create(TaskModel task) throws IOException {
        List<TaskModel> taskModels = new ArrayList<>();
        int maxId = 0;
        try {
            taskModels = readSerialData();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        catch (ClassNotFoundException c){
            c.printStackTrace();
        }
        UUID newUUID = UUID.randomUUID();
        task.setId(newUUID);
        taskModels.add(task);
        writeSerialData(taskModels);
        return newUUID;
    }

    /***
     * Finds the desired task by its id and rewrites it with updated data
     * @param task
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public boolean update(TaskModel task) throws IOException, ClassNotFoundException {
        List<TaskModel> taskModels = readSerialData();
        TaskModel t = findTaskByID(taskModels,task.getId());
        if(t!=null) {
            int position = taskModels.indexOf(t);
            taskModels.set(position,task);
            writeSerialData(taskModels);
            return true;
        }
        return false;
    }

    /***
     * Finds task by its id and removes it
     * @param taskId
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public boolean delete(UUID taskId) throws IOException, ClassNotFoundException {
        List<TaskModel> taskModels = readSerialData();
        TaskModel t = findTaskByID(taskModels,taskId);
        if(t!=null) {
            taskModels.remove(t);
            writeSerialData(taskModels);
            return true;
        }
        return false;
    }

    /***
     * Restores the last deleted task (the program gives you the option to restore the deleted task until the snackbar dissapears, or you delete another task)
     * @param task
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public boolean restore(TaskModel task) throws IOException, ClassNotFoundException {
        List<TaskModel> taskModels = readSerialData();

        if(taskModels!=null) {
            taskModels.add(task);
            writeSerialData(taskModels);
            return true;
        } else {
            taskModels = new ArrayList<TaskModel>();
            taskModels.add(task);
            writeSerialData(taskModels);
            return true;
        }

    }

    /***
     * Reads data from a file
     * @return taskModel
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private List<TaskModel> readSerialData() throws IOException, ClassNotFoundException {
        List<TaskModel> taskModel;
        try {
            FileInputStream fos = owner.openFileInput("taskList.dat");
            ObjectInputStream objOut = new ObjectInputStream(fos);
            taskModel = (List<TaskModel>) objOut.readObject();
            objOut.close();
            fos.close();
        }catch (Exception e){
            taskModel = new ArrayList<TaskModel>();
        }
        return taskModel;
    }

    /***
     * Writes data into a file
     * @param taskModels
     * @throws IOException
     */
    private void writeSerialData(List<TaskModel> taskModels) throws IOException{
        FileOutputStream fos = owner.openFileOutput("taskList.dat",
                Context.MODE_PRIVATE);
        ObjectOutputStream objOut = new ObjectOutputStream(fos);
        objOut.writeObject(taskModels);
        objOut.close();
        fos.close();
    }


    /***
     * Finds given task by its id
     * @param taskModels
     * @param taskID
     * @return
     */
    private TaskModel findTaskByID (List<TaskModel> taskModels,UUID taskID) {
        TaskModel task = taskModels
                .stream()
                .filter(t -> taskID.equals(t.getId())).findFirst().orElse(null);
        return task;
    }
}
