package com.jecna.task.service;


import android.app.Activity;
import android.content.Context;
import com.jecna.task.model.TaskDTO;
import com.jecna.task.model.TaskModel;


import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public void synchTasks(TaskDTO[] taskDTOS) throws IOException, ClassNotFoundException, ParseException {
        List<TaskModel> taskModel = readSerialData();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        for (TaskDTO t:taskDTOS){
            boolean isHere = false;
            for (TaskModel tm : taskModel){
                if (t.getId().equals(tm.getId())){
                    isHere = true;
                }
            }
            if(!isHere){
                taskModel.add(new TaskModel(t.getId(),t.getName(),t.getDescription(),new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(t.getDateFrom()),new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(t.getDateTo()),TaskModel.Status.valueOf(t.getStatus()),TaskModel.Priority.valueOf(t.getPriority())));
            }
        }
        writeSerialData(taskModel);

        SingetonToken singletonToken = com.jecna.task.service.SingetonToken.getInstance();
        String token = singletonToken.getToken();
        ServerClientImpl serverClient = new ServerClientImpl(token);
        List<TaskDTO> taskDTOS1 = new ArrayList<TaskDTO>();
        for(TaskModel t : taskModel){
            boolean isThere = false;
            for (TaskDTO td : taskDTOS) {
                if (t.getId().equals(td.getId())){
                    isThere = true;
                }
            }
            if(!isThere) {
                taskDTOS1.add(new TaskDTO(t.getId(),t.getName(),t.getDescription(),dateFormat.format(t.getDateFrom()), dateFormat.format(t.getDateTo()),t.getStatus().getValue(),t.getPriority().getValue()));
            }
        }
        TaskDTO[] taskDTOS2 = new TaskDTO[taskDTOS1.size()];
        taskDTOS1.toArray(taskDTOS2);
        try {
            serverClient.setSaveTaskListener(new ServerClientImpl.SaveTaskListener() {
                @Override
                public void onSaveTaskFinish() {

                }
            });
            serverClient.SaveTask(taskDTOS2);
        } finally {
        }
    }
}
