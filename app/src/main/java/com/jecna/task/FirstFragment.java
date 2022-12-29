package com.jecna.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.jecna.task.databinding.FragmentFirstBinding;
import com.jecna.task.model.TaskModel;
import com.jecna.task.service.DataService;
import com.jecna.task.service.DataServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirstFragment extends Fragment implements ListItemClickListener {

    private FragmentFirstBinding binding;
    private RecyclerView recycleView;
    private List<TaskModel> taskModelList;
    private View v;
    private DataService dataService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if(bundle!=null) {
            TaskModel taskModel = (TaskModel) bundle.getSerializable("task");
            if(taskModel!=null){
             if(taskModel.getId()==-1){
                 try {
                     dataService.create(taskModel);
                 }catch (IOException ioe){
                     Snackbar.make(getView(), "Initial file not found", Snackbar.LENGTH_LONG)
                             .setAction("Action", null).show();
                 }catch (ClassNotFoundException c){
                     Snackbar.make(getView(), "Class not found", Snackbar.LENGTH_LONG)
                             .setAction("Action", null).show();
                 }
             }else{
                 try {
                     dataService.update(taskModel);
                 }catch (IOException ioe){
                     Snackbar.make(getView(), "Initial file not found", Snackbar.LENGTH_LONG)
                             .setAction("Action", null).show();
                 }catch (ClassNotFoundException c){
                     Snackbar.make(getView(), "Class not found", Snackbar.LENGTH_LONG)
                             .setAction("Action", null).show();
                 }
             }
            }
        }

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        dataService = new DataServiceImpl(getActivity());
        //v = inflater.inflate(R.layout.fragment_first,container,false);
        v = binding.getRoot();
        recycleView = (RecyclerView) v.findViewById(R.id.task_recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecycleViewAdapter viewAdapter = new RecycleViewAdapter(getContext(), taskModelList,this);
        recycleView.setAdapter(viewAdapter);
        return binding.getRoot();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskModelList = new ArrayList<TaskModel>();
        try {
            taskModelList = dataService.read();

        }catch (IOException ioe){
            Snackbar.make(getView(), "Initial file not found", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        catch (ClassNotFoundException c){
            Snackbar.make(getView(), "Class not found", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }catch (Exception c){
            //Snackbar.make(getView(), c.getMessage(), Snackbar.LENGTH_LONG)
              //      .setAction("Action", null).show();
        }
        //read from file instead
       // taskModelList.add(new TaskModel("task 1","task 1 des",new Date(),new Date(),TaskModel.Status.NOT_STARTED,TaskModel.Priority.LOW));
        //taskModelList.add(new TaskModel("task 2","task 2 des",new Date(),new Date(),TaskModel.Status.ONGOING,TaskModel.Priority.MEDIUM));
        //taskModelList.add(new TaskModel("task 3","task 3",new Date(),new Date(),TaskModel.Status.ONGOING,TaskModel.Priority.HIGH));
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.getBoolean("editable", true);
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void clickPosition(int position, int id) {
        switch (id) {
            case R.id.name_task:
                TaskModel actualTask = taskModelList.get(position);
                Bundle bundle = new Bundle();
                bundle.getBoolean("editable", false);
                bundle.putSerializable("task", actualTask);
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
                break;
        }

    }
}