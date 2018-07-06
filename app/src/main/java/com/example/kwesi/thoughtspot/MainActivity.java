package com.example.kwesi.thoughtspot;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase groupsDatabase;
    SQLiteDatabase activitiesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //Start Database
        initDatabases();



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
}
