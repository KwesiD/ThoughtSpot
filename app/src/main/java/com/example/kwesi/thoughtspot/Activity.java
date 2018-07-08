package com.example.kwesi.thoughtspot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Kwesi on 7/5/2018.
 */

public class Activity implements Parcelable {

    private String name;
    private String location;
    private String description;
    private int min;
    private int max;
    private ArrayList<Uri> photos;
    private ArrayList<String> tags;

    public Activity(String name, String location, String description, String min, String max, ArrayList<Uri> photos, ArrayList<String> tags){
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

    public ArrayList<Uri> getPhotos() {
        return photos;
    }

    public ArrayList<String> getTags() {
        return tags;
    }


    //Parcelable constructor
    public Activity(Parcel in){
        name = in.readString();
        location = in.readString();
        description = in.readString();
        min = in.readInt();
        max = in.readInt();
        photos = (ArrayList<Uri>) in.readSerializable();
        tags = (ArrayList<String>) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeInt(min);
        dest.writeInt(max);
        dest.writeSerializable(photos);
        dest.writeSerializable(tags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Activity createFromParcel(Parcel in){
            return new Activity(in);
        }

        public Activity[] newArray(int size){
            return new Activity[size];
        }
    };

    //TODO: Create method to return content values for database insertion
}
