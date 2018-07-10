package com.example.kwesi.thoughtspot;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Activity(String name, String location, String description, String min, String max, ArrayList<Uri> photos, ArrayList<String> tags) {
        this.name = name;
        this.location = location;
        this.description = description;
        if (min.equals("")) {
            this.min = 0;
        } else {
            this.min = Integer.parseInt(min);
        }
        if (max.equals("")) {
            this.max = 0;
        } else {
            this.max = Integer.parseInt(max);
        }
        this.photos = photos;
        this.tags = tags;
    }

    public Activity(String name, String location, String description, int min, int max, ArrayList<Uri> photos, ArrayList<String> tags) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.min = min;
        this.max = max;
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
    public Activity(Parcel in) {
        name = in.readString();
        location = in.readString();
        description = in.readString();
        min = in.readInt();
        max = in.readInt();
        photos = StringArrayToUri((ArrayList<String>) in.readSerializable());
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
        dest.writeSerializable(UriArrayToString(photos));
        dest.writeSerializable(tags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Activity createFromParcel(Parcel in) {
            return new Activity(in);
        }

        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };

    private ArrayList<String> UriArrayToString(ArrayList<Uri> uriList){
        ArrayList<String> stringList = new ArrayList<String>(uriList.size());
        for(int i = 0;i < uriList.size();i++){
            stringList.add(uriList.get(i).toString());
        }
        return stringList;
    }

    private ArrayList<Uri> StringArrayToUri(ArrayList<String> stringList){
        ArrayList<Uri> uriList = new ArrayList<Uri>(stringList.size());
        for(int i = 0;i < stringList.size();i++){
            uriList.add(Uri.parse(stringList.get(i)));
        }
        return uriList;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("location", location);
        values.put("locationType", getLocationType(location));
        values.put("description", description);
        values.put("priceMin", min);
        values.put("priceMax", max);
        values.put("photoList",toJSONString(photos,"photos"));
        values.put("tagList",toJSONString(tags,"tags"));

        return values;
    }

    public int getLocationType(String location) {
        return 0; //TODO:Temp
    }

    public String toJSONString(ArrayList arrayList,String jsonName){
        JSONObject json = new JSONObject();
        try {
            json.put(jsonName, arrayList);
        }
        catch(JSONException j){
            j.printStackTrace();
        }
        return json.toString();
    }

}


