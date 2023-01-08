package com.jecna.task;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        v = binding.getRoot();
        recycleView = (RecyclerView) v.findViewById(R.id.task_recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecycleViewAdapter viewAdapter = new RecycleViewAdapter(getContext(), taskModelList, this);
        recycleView.setAdapter(viewAdapter);
        enableSwipeToDeleteAndUndo(viewAdapter, recycleView);
        return binding.getRoot();

    }


    public void setSort(int sortId) {
        switch (sortId){
            case 1: break;
            case 2: break;
            default: break;
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskModelList = new ArrayList<TaskModel>();

        try {
            dataService = new DataServiceImpl(getActivity());
            taskModelList = dataService.read();
            Bundle bundle = getArguments();
            if (bundle != null) {
                TaskModel taskModel = (TaskModel) bundle.getSerializable("task");
                if (taskModel != null) {
                    if (taskModel.getId() == -1) {
                        dataService.create(taskModel);
                        taskModelList = dataService.read();
                    } else {
                        dataService.update(taskModel);
                        taskModelList = dataService.read();

                    }
                }
            }
        }
        catch (IOException ioe) {
            Toast.makeText(getContext(), ioe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (ClassNotFoundException c) {
            Toast.makeText(getContext(), c.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception c) {
            Toast.makeText(getContext(), c.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.getBoolean("editable", true);
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
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
        TaskModel actualTask = taskModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.getBoolean("editable", false);
        bundle.putSerializable("task", actualTask);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
    }

    private void enableSwipeToDeleteAndUndo(RecycleViewAdapter viewAdapter, RecyclerView recycleView) {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final TaskModel item = (TaskModel) viewAdapter.getData().get(position);

                viewAdapter.removeItem(position);
                try {
                    dataService.delete(item.getId());
                } catch (IOException ioe) {
                    Toast.makeText(getContext(), ioe.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ClassNotFoundException c) {
                    Toast.makeText(getContext(), c.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception c) {
                    Toast.makeText(getContext(), c.getMessage(), Toast.LENGTH_LONG).show();
                }


                Snackbar snackbar = Snackbar.make(getView(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewAdapter.restoreItem(item, position);
                        recycleView.scrollToPosition(position);
                        dataService = new DataServiceImpl(getActivity());
                        try {
                            dataService.restore(item);
                        } catch (IOException ioe) {
                            Toast.makeText(getContext(), ioe.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (ClassNotFoundException c) {
                            Toast.makeText(getContext(), c.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recycleView);
    }

}
