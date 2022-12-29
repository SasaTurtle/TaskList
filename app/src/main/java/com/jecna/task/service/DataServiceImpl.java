package com.jecna.task.service;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
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

    private List<TaskModel> readSerialData() throws IOException, ClassNotFoundException {
        List<TaskModel> taskModel;
        FileInputStream fos = owner.openFileInput("taskList.dat");
        ObjectInputStream objOut = new ObjectInputStream(fos);
        taskModel = (List<TaskModel>) objOut.readObject();
        objOut.close();
        fos.close();
        /*FileInputStream f = new FileInputStream(getFilePath());
        ObjectInputStream o = new ObjectInputStream(f);

        taskModel = (ArrayList) o.readObject();

        o.close();
        f.close();
*/
        return taskModel;
    }

    private void writeSerialData(List<TaskModel> taskModels) throws IOException{
        FileOutputStream fos = owner.openFileOutput("taskList.dat",
                Context.MODE_PRIVATE);
        ObjectOutputStream objOut = new ObjectOutputStream(fos);
        objOut.writeObject(taskModels);
        objOut.close();
        fos.close();
        /*FileOutputStream f = new FileOutputStream(getFilePath());
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(taskModels);

        o.close();
        f.close();
         */
    }

    private int maxID (List<TaskModel> taskModels) {
        TaskModel maxTask = taskModels
                .stream()
                .max(Comparator.comparing(TaskModel::getId))
                .orElseThrow(NoSuchElementException::new);
        return maxTask.getId();
    }

    private TaskModel findTaskByID (List<TaskModel> taskModels,int taskID) {
        TaskModel task = taskModels
                .stream()
                .filter(t -> taskID == t.getId()).findFirst().orElse(null);
        return task;
    }

    private String getFilePath(){
        String path =  Environment.getStorageDirectory() + "/com.example.tasklist/dataList.dat";
        return path;
    }
}
