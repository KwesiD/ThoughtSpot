package com.example.kwesi.thoughtspot;

import android.content.Intent;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Kwesi on 7/5/2018.
 */

public class Activity {

    private String name;
    private String location;
    private String description;
    private int min;
    private int max;
    private ArrayList<Bitmap> photos;
    private ArrayList<String> tags;

    Activity(String name, String location, String description, String min, String max, ArrayList<Bitmap> photos, ArrayList<String> tags){
        this.name = name;
        this.location = location;
        this.description = description;
        if(min.equals("")){
            this.min = 0;
        }
        else{
            this.min = Integer.parseInt(min);
        }
        if(max.equals("")){
            this.max = 0;
        }
        else{
            this.max = Integer.parseInt(max);
        }
        this.photos = photos;
        this.tags = tags;
    }
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public ArrayList<Bitmap> getPhotos() {
        return photos;
    }

    public ArrayList<String> getTags() {
        return tags;
    }


}
