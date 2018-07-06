package com.example.kwesi.thoughtspot;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kwesi on 7/4/2018.
 */

public class EditGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group_layout);
        //the intent that called this activity
        Intent caller = getIntent();
        //checks for request code. If DNE, default to New Group
        int requestCode = caller.getIntExtra("requestCode",IntentCodes.NEW_GROUP);

        if(requestCode == IntentCodes.NEW_GROUP){ //If we are creating new group
            createNewGroup();
        }
        else if(requestCode == IntentCodes.EDIT_GROUP){ //If we are editing an old group
            //TODO: Handle edit group
        }
    }

    private void createNewGroup(){

        SQLiteDatabase activitiesDatabase = openOrCreateDatabase("Activities",MODE_PRIVATE,null); //TODO: Remove and switch to openhandler later

    }
}
