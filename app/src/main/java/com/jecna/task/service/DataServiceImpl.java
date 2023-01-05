package com.jecna.task.service;


import android.app.Activity;
import android.content.Context;
import com.jecna.task.model.TaskModel;


import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

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
    public int create(TaskModel task) throws IOException {
        List<TaskModel> taskModels = new ArrayList<>();
        int maxId = 0;
        try {
            taskModels = readSerialData();
            maxId = maxID(taskModels)+1;
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        catch (ClassNotFoundException c){
            c.printStackTrace();
        }
        task.setId(maxId);
        taskModels.add(task);
        writeSerialData(taskModels);
        return maxId;
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
    public boolean delete(int taskId) throws IOException, ClassNotFoundException {
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
        FileInputStream fos = owner.openFileInput("taskList.dat");
        ObjectInputStream objOut = new ObjectInputStream(fos);
        taskModel = (List<TaskModel>) objOut.readObject();
        objOut.close();
        fos.close();
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
     * Finds the highest task id
     * @param taskModels
     * @return
     */
    private int maxID (List<TaskModel> taskModels) {
        TaskModel maxTask = taskModels
                .stream()
                .max(Comparator.comparing(TaskModel::getId))
                .orElseThrow(NoSuchElementException::new);
        return maxTask.getId();
    }

    /***
     * Finds given task by its id
     * @param taskModels
     * @param taskID
     * @return
     */
    private TaskModel findTaskByID (List<TaskModel> taskModels,int taskID) {
        TaskModel task = taskModels
                .stream()
                .filter(t -> taskID == t.getId()).findFirst().orElse(null);
        return task;
    }
}
