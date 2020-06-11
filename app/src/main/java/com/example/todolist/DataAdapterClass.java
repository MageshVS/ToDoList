package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapterClass extends RecyclerView.Adapter<DataAdapterClass.ViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList array_id, array_label, array_date, array_time, array_notes;
    Animation animation;
    int position;
    public DataAdapterClass(Activity activity, Context context, ArrayList array_id,
                            ArrayList array_label, ArrayList array_date, ArrayList array_time, ArrayList array_notes) {
        this.context = context;
        this.array_id = array_id;
        this.array_label = array_label;
        this.array_date = array_date;
        this.array_time = array_time;
        this.array_notes = array_notes;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our custom recyclerView layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, final int position) {
        this.position = position;
        holder.labelView.setText(String.valueOf(array_label.get(position)));
        holder.dateView.setText(String.valueOf(array_date.get(position)));
        holder.timeView.setText(String.valueOf(array_time.get(position)));
        holder.noteView.setText(String.valueOf(array_notes.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when user clicks on any one of the recyclerView
                //this will start an intent to updateActivity with entire data of the selected View
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("ID",String.valueOf(array_id.get(position)));
                intent.putExtra("LABEL",String.valueOf(array_label.get(position)));
                intent.putExtra("DATE",String.valueOf(array_date.get(position)));
                intent.putExtra("TIME",String.valueOf(array_time.get(position)));
                intent.putExtra("NOTES",String.valueOf(array_notes.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView labelView, dateView, timeView, noteView;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            labelView = itemView.findViewById(R.id.re_label);
            dateView = itemView.findViewById(R.id.re_date);
            timeView = itemView.findViewById(R.id.re_time);
            noteView = itemView.findViewById(R.id.re_note);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            //setting animation for the items in recyclerView
            animation = AnimationUtils.loadAnimation(context,R.anim.translate_anim);
            mainLayout.setAnimation(animation);
        }
    }
}