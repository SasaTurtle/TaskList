package com.jecna.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.jecna.task.model.TaskModel;

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

        holder.name.setText(taskModelList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_task);
            name.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            listItemClickListener.clickPosition(getAdapterPosition(), view.getId());
        }
    }
}