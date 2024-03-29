package com.jecna.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.jecna.task.databinding.FragmentSecondBinding;
import com.jecna.task.model.TaskModel;

import java.util.Date;

public class SecondFragment extends Fragment {

    private TaskModel taskModel;
    private EditText name;
    private EditText description;
    private Spinner priority;
    private Spinner status;
    private FragmentSecondBinding binding;
    private TextView startDate;
    private TextView endDate;
    private TextView startTime;
    private TextView endTime;
    private boolean isEditable = true;

    private java.text.DateFormat dateFormat;

    /**
     * Instantiatets all items in the second fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dateFormat = android.text.format.DateFormat.getDateFormat(this.getActivity().getApplicationContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskModel = (TaskModel) bundle.getSerializable("task");
            if (taskModel == null) {
                taskModel = new TaskModel("", "", new Date(), new Date(), TaskModel.Status.NOT_STARTED, TaskModel.Priority.LOW);
            } else {
                ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar != null)
                    actionBar.setTitle(String.format("Edit Task"));
            }

            isEditable = bundle.getBoolean("editable");
        } else {
            taskModel = new TaskModel("", "", new Date(), new Date(), TaskModel.Status.NOT_STARTED, TaskModel.Priority.LOW);
        }

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        //Sets edit boxes
        name = (EditText) binding.getRoot().findViewById(R.id.taskName);
        name.setText(taskModel.getName());
        description = (EditText) binding.getRoot().findViewById(R.id.taskDescription);
        description.setText(taskModel.getDescription());

        //Sets dropdowns
        priority = (Spinner) binding.getRoot().findViewById(R.id.priorityTask);
        status = (Spinner) binding.getRoot().findViewById(R.id.statusTask);
        String[] priorityEnum = new String[]{"Low", "Medium", "High"};
        String[] statusEnum = new String[]{"Not Started", "Ongoing", "Finished"};
        ArrayAdapter<String> adapterPriority = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, priorityEnum);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, statusEnum);
        priority.setAdapter(adapterPriority);
        status.setAdapter(adapterStatus);
        status.setSelection(taskModel.getStatus().getValue());
        priority.setSelection(taskModel.getPriority().getValue());

        //Sets date
        startDate = (TextView) binding.getRoot().findViewById(R.id.startDate);
        endDate = (TextView) binding.getRoot().findViewById(R.id.endDate);
        startTime = (TextView) binding.getRoot().findViewById(R.id.startTime);
        endTime = (TextView) binding.getRoot().findViewById(R.id.endTime);
        startDate.setText(dateFormat.format(taskModel.getDateFrom().getTime()));
        endDate.setText(dateFormat.format(taskModel.getDateTo().getTime()));

        //Sets time
        Calendar c = Calendar.getInstance();
        c.setTime(taskModel.getDateFrom());
        startTime.setText(String.format("%02d:%02d",c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));
        c.setTime(taskModel.getDateTo());
        endTime.setText(String.format("%02d:%02d",c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Save button on click
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update taskModel
                taskModel.setName(name.getText().toString());
                taskModel.setDescription(description.getText().toString());
                Long p = (Long) priority.getSelectedItemId();
                taskModel.setPriority(TaskModel.Priority.values()[p.intValue()]);
                Long s = (Long) status.getSelectedItemId();
                taskModel.setStatus(TaskModel.Status.values()[s.intValue()]);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", taskModel);
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment, bundle);
            }
        });

        //Date calendar on click
        binding.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                c.setTime(taskModel.getDateFrom());
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    //Ok button in calendar
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                        String date = dateFormat.format(newDate.getTime());
                        startDate.setText(date);
                        taskModel.setDateFrom(newDate.getTime());
                    }

                }, year, month, day);
                dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
            }

        });

        //Date calendar on click
        binding.endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                c.setTime(taskModel.getDateTo());
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                        String date = dateFormat.format(newDate.getTime());
                        endDate.setText(date);
                        taskModel.setDateTo(newDate.getTime());
                    }

                }, year, month, day);
                dialog.getDatePicker().setMinDate(taskModel.getDateFrom().getTime());
                dialog.show();
            }

        });

        //Time clock on click
        binding.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                currentTime.setTime(taskModel.getDateFrom());
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH), selectedHour, selectedMinute);
                        startTime.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
                        taskModel.setDateFrom(newDate.getTime());
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //Time clock on click
        binding.endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                currentTime.setTime(taskModel.getDateTo());
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar newDate = Calendar.getInstance();
                        endTime.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
                        newDate.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH), selectedHour, selectedMinute);
                        taskModel.setDateTo(newDate.getTime());

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}