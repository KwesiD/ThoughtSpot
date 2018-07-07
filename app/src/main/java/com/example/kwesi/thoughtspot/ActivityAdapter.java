package com.example.kwesi.thoughtspot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kwesi on 7/7/2018.
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder>{
    private ArrayList<Activity> activityList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout activityCard;
        public ViewHolder(LinearLayout card){
            super(card);
            activityCard = card;

        }
    }

    public ActivityAdapter(ArrayList<Activity> activities){
        activityList = activities;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LinearLayout activityView = (LinearLayout)inflater.inflate(R.layout.activity_layout,null);
        ViewHolder holder = new ViewHolder(activityView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity currActivity = activityList.get(position);
        LinearLayout activityView = holder.activityCard;
        String name = currActivity.getName();
        String location = currActivity.getLocation();
        String description = currActivity.getDescription();
        TextView nameView = activityView.findViewById(R.id.activity_title);
        TextView descView = activityView.findViewById(R.id.activity_description);
        nameView.setText(name);
        descView.setText(description);
        //TODO: Get user location information
        /*if(location permission granted){
            TextView distView = activityView.findViewById(R.id.activity_distance);
            currDist = |location - user location|
            distView.setText(currDist);
            //Refresh distView ever so often with new distance
        }
        */


    }


    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public void add(Activity newActivity,boolean notify){
        activityList.add(newActivity);
        if(notify) {
            this.notifyItemInserted(activityList.size() - 1);
        }
    }
}