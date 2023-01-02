package com.jecna.task;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.jecna.task.model.TaskModel;
import com.jecna.task.service.DataService;
import com.jecna.task.service.DataServiceImpl;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    Context context;
    List<TaskModel> taskModelList;
    private static ListItemClickListener listItemClickListener;

    public RecycleViewAdapter(Context context, List<TaskModel> contactList, ListItemClickListener listItemClickListener) {
        this.context = context;
        this.taskModelList = contactList;
        this.listItemClickListener = listItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String[] priorityEnum = new String[]{ "Low", "Medium","High"};
        String[] statusEnum = new String[]{"Not Started","Ongoing","Finished"};

        holder.name.setText(taskModelList.get(position).getName());
        holder.priority.setText(priorityEnum[taskModelList.get(position).getPriority().getValue()]);
        switch (taskModelList.get(position).getPriority()) {
            case LOW:
                holder.priority.setTextColor(ContextCompat.getColor(context, R.color.low));
                break;
            case MEDIUM:
                holder.priority.setTextColor(ContextCompat.getColor(context, R.color.medium));
                break;
            case HIGH:
                holder.priority.setTextColor(ContextCompat.getColor(context, R.color.high));
                break;
            default:
                holder.priority.setTextColor(ContextCompat.getColor(context, R.color.low));
                break;
        }

        holder.status.setText(statusEnum[taskModelList.get(position).getStatus().getValue()]);
        switch (taskModelList.get(position).getStatus()) {
            case NOT_STARTED:
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.not_started));
                break;
            case ONGOING:
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.high));
                break;
            case FINISHED:
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.low));
                break;
            default:
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.not_started));
                break;
        }


    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    public void removeItem(int position) {
        this.taskModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(TaskModel item, int position) {
        this.taskModelList.add(position, item);
        notifyItemInserted(position);
    }
    public List<TaskModel> getData() {
        return taskModelList;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView priority;
        TextView status;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_task);
            name.setOnClickListener(this);
            priority = (TextView) itemView.findViewById(R.id.priority_task);
            priority.setOnClickListener(this);
            status = (TextView) itemView.findViewById(R.id.status_task);
            status.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            listItemClickListener.clickPosition(getAdapterPosition(), view.getId());
        }
    }
}