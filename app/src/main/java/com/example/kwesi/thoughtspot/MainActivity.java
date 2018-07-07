package com.example.kwesi.thoughtspot;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase groupsDatabase;
    private SQLiteDatabase activitiesDatabase;
    private RecyclerView activityScroller;
    private ActivityAdapter activityAdapter;
    private RecyclerView.LayoutManager activityLayoutManager;
    private RecyclerView groupScroller;
    private ArrayList<Activity> activityList; //adapter updates this list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        activityList = new ArrayList<Activity>();

        //Start Database
        initDatabases();

        activityScroller = findViewById(R.id.card_scroller);
        activityLayoutManager = new LinearLayoutManager(this);
        activityScroller.setLayoutManager(activityLayoutManager);

        activityAdapter = new ActivityAdapter(getActivitiesFromDatabase());
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
        groupsDatabase = openOrCreateDatabase("Groups",MODE_PRIVATE,null);
        activitiesDatabase = openOrCreateDatabase("Activities",MODE_PRIVATE,null);

        groupsDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS TASKS " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR," +
                        "description VARCHAR," +
                        "size INTEGER," +
                        "activityList VARCHAR," + //JSON String of arraylist
                        "createdOn DATETIME," +
                        "lastUpdated DATETIME);"
        );

        activitiesDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS TASKS " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name VARCHAR," +
                        "location VARCHAR," +  //Either address or longitude + latitude
                        "locationType TINYINT(1)," +  //Either 0 or 1 (Address or long/lat)
                        "description VARCHAR," +
                        "priceMin INTEGER," +
                        "priceMax INTEGER," +
                        "photoList VARCHAR," + //JSON String of arraylist
                        "tagList VARCHAR," +
                        "createdOn DATETIME," +
                        "lastUpdated DATETIME);" //JSON String of arraylist
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
            ContentValues values = new ContentValues();
            String name = data.getStringExtra("name");
            String location = data.getStringExtra("location");
            String description = data.getStringExtra("description");
            String min = data.getStringExtra("min");
            String max = data.getStringExtra("max");
            //ArrayList<Bitmap> photos = data.getParcelableArrayListExtra("photos"); //TODO: Change <Bitmap> to <URI>
            ArrayList<String> tags = data.getStringArrayListExtra("tags"); //TODO: JSON both photos and tags

            RecyclerView scroller = findViewById(R.id.card_scroller); //TODO: What happens if i have 2 of these in different tabs?
            Activity currActivity = new Activity(name,location,description,min,max,null,tags);
            addNewActivity(currActivity);

        }
    }

    private ArrayList<Activity> getActivitiesFromDatabase(){
        //TODO: Temp. For now, return activity arraylist.
        return activityList;
    }

    private void addNewActivity(Activity activity){
        activityAdapter.add(activity,true);
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
