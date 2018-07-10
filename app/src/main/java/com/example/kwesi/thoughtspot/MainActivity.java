package com.example.kwesi.thoughtspot;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private SQLiteDatabase groupsDatabase;
//    private SQLiteDatabase activitiesDatabase;
    private SQLiteDatabase database;
    private RecyclerView activityScroller;
    private ActivityAdapter activityAdapter;
    private RecyclerView.LayoutManager activityLayoutManager;
    private RecyclerView groupScroller;
    //private ArrayList<Activity> activityList; //adapter updates this list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //Start Database
        initDatabases();
        ArrayList<Activity> activityList = getActivitiesFromDatabase();

        activityScroller = findViewById(R.id.card_scroller);
        activityLayoutManager = new LinearLayoutManager(this);
        activityScroller.setLayoutManager(activityLayoutManager);

        activityAdapter = new ActivityAdapter(activityList);
        activityScroller.setAdapter(activityAdapter);
        //TODO: Create tab for groups and put its own scroller there



        //FAB Creates new group
        FloatingActionButton groupfab = (FloatingActionButton) findViewById(R.id.new_group_fab);
        groupfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGroupIntent = new Intent(MainActivity.this, EditGroupActivity.class);
                startActivityForResult(newGroupIntent, Codes.NEW_GROUP);
            }
        });
        //FAB Creates new group
        FloatingActionButton activityfab = (FloatingActionButton) findViewById(R.id.new_activity_fab);
        activityfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGroupIntent = new Intent(MainActivity.this, EditActivitiesActivity.class);
                startActivityForResult(newGroupIntent, Codes.NEW_ACTIVITY);
            }
        });


    }

    /**
     * Initialize the Groups and Activities databases
     */
    public void initDatabases(){
        //Create Databases for groups and activities
        //groupsDatabase = openOrCreateDatabase("Groups",MODE_PRIVATE,null);
        database = openOrCreateDatabase("Activities",MODE_PRIVATE,null);

        database.execSQL(
                "CREATE TABLE IF NOT EXISTS Groups " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR," +
                        "description VARCHAR," +
                        "size INTEGER," +
                        "activityList VARCHAR," + //JSON String of arraylist
                        "createdOn DATETIME," +
                        "lastUpdated DATETIME);"
        );

        database.execSQL(
                "CREATE TABLE IF NOT EXISTS Activities " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name VARCHAR," +
                        "location VARCHAR," +  //Either address or longitude + latitude
                        "locationType TINYINT(1)," +  //Either 0 or 1 (Address or long/lat)
                        "description VARCHAR," +
                        "priceMin INTEGER," +
                        "priceMax INTEGER," +
                        "photoList VARCHAR," + //JSON String of arraylist
                        "tagList VARCHAR" +  //JSON String of arraylist
                        ");"
                       /* "createdOn DATETIME," +
                        "lastUpdated DATETIME);"*/
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this,requestCode,Toast.LENGTH_SHORT).show();
        switch(requestCode){
            case Codes.NEW_ACTIVITY:
                newActivityHandler(resultCode,data);
                break;
        }
    }

    private void newActivityHandler(int resultCode,Intent data){
        if(resultCode == RESULT_OK){
            RecyclerView scroller = findViewById(R.id.card_scroller); //TODO: What happens if i have 2 of these in different tabs?
            //Activity currActivity = new Activity(name,location,description,min,max,null,tags);
            Activity currActivity = data.getExtras().getParcelable("activity");
            addNewActivity(currActivity);

        }
    }

    private ArrayList<Activity> getActivitiesFromDatabase(){
        ArrayList<Activity> activityList = new ArrayList<Activity>();

        Cursor cursor = database.rawQuery("SELECT * FROM Activities",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String name = cursor.getString(1);
            String location = cursor.getString(2);
            String description = cursor.getString(4);
            int min = cursor.getInt(5);
            int max = cursor.getInt(6);
            ArrayList<Uri> photos = new ArrayList<Uri>();
            ArrayList<String> tags = new ArrayList<String>();
            decodePhotosJSON(cursor.getString(7),photos,"photos");
            decodeTagsJSON(cursor.getString(8),tags,"tags");
            activityList.add(new Activity(name,location,description,min,max,photos,tags));
            cursor.moveToNext();
        }
        return activityList;
    }

    private void decodePhotosJSON(String jsonString,ArrayList arrayList,String label){
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonString);
            String arrayString = (String)jsonObject.get(label);
            arrayString.substring(1,arrayString.length()-1);
            String[] stringList = arrayString.split(",");
            for(int i = 0;i < stringList.length;i++){
                arrayList.add(Uri.parse(stringList[i].trim()));
            }
        }
        catch (JSONException j){
            j.printStackTrace();
        }
    }
    private void decodeTagsJSON(String jsonString,ArrayList arrayList,String label){
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonString);
            String arrayString = (String)jsonObject.get(label);
            arrayString.substring(1,arrayString.length()-1);
            String[] stringList = arrayString.split(",");
            for(int i = 0;i < stringList.length;i++){
                arrayList.add(stringList[i].trim());
            }
        }
        catch (JSONException j){
            j.printStackTrace();
        }
    }


    private void addNewActivity(Activity activity){
        activityAdapter.add(activity,true);
        database.insert("Activities",null,activity.getContentValues());
        //Log.e("addnewactivity",activity.getPhotos().toString());
    }

    private View createActivityCard(String name,String location,String description){
        View activityView;
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activityView = inflater.inflate(R.layout.activity_layout,null);
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
        return activityView;




    }
}
