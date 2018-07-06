package com.example.kwesi.thoughtspot;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Kwesi on 7/5/2018.
 */

public class EditActivitiesActivity extends AppCompatActivity {
    private String activityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity_layout);
        //the intent that called this activity
        Intent caller = getIntent();
        //checks for request code. If DNE, default to New Activity
        int requestCode = caller.getIntExtra("requestCode",IntentCodes.NEW_ACTIVITY);

        Button addPhotos = findViewById(R.id.edit_add_photos_button);
        addPhotos.setOnClickListener(addPhotoListener());

        if(requestCode == IntentCodes.NEW_ACTIVITY){ //If we are creating new activity
            //TODO: Get values from input boxes
            Button submitButton = findViewById(R.id.edit_activity_submit);
            submitButton.setOnClickListener(submitListener());
        }
        else if(requestCode == IntentCodes.EDIT_ACTIVITY){ //If we are editing an old activity
            //TODO: Handle edit activity
        }
    }

    private void createNewActivity(){

    }

    private Bitmap[] getPhotos(HorizontalScrollView photoView){
        return null;
    }

    private String[] getTags(GridLayout tagGrid){
        return null;
    }

    private void setParameters(String name,String location,String description,String min,String max,Bitmap[] photos,String[] tags){

    }

    private View.OnClickListener submitListener(){
        final View.OnClickListener submitListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Required fields
                String name = ((EditText)findViewById(R.id.edit_activity_name)).getText().toString().trim();
                String location = ((EditText)findViewById(R.id.edit_activity_location)).getText().toString().trim();
                if(name.equals("")){
                   Toast.makeText(view.getContext(),"Activity Name Required",Toast.LENGTH_SHORT);
                }
                else if(location.equals("")){
                    Toast.makeText(view.getContext(),"Activity Location Required",Toast.LENGTH_SHORT);
                }
                else if(name.equals("") && location.equals("")){
                    Toast.makeText(view.getContext(),"Activity Name and Activity Location Required",Toast.LENGTH_SHORT);
                }
                else{
                    String description = ((EditText)findViewById(R.id.edit_activity_description)).getText().toString().trim();
                    String min = ((EditText)findViewById(R.id.price_range_min)).getText().toString().trim();
                    String max = ((EditText)findViewById(R.id.price_range_max)).getText().toString().trim();
                    Bitmap[] photos = getPhotos((HorizontalScrollView)findViewById(R.id.edit_photo_scroller));
                    String[] tags = getTags((GridLayout)findViewById(R.id.edit_tag_grid));
                    setParameters(name,location,description,min,max,photos,tags);
                }
          }
        };

        return submitListener;
    }

    private View.OnClickListener addPhotoListener(){
        View.OnClickListener photoListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(view);
            }

            private void openDialog(View view){
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Upload Photo")
                        .setMessage("How do you want to upload a photo?")
                        .setPositiveButton("Camera",cameraListener());
                dialog.show();
            }

            private DialogInterface.OnClickListener cameraListener(){
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File tempPhoto = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
                        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempPhoto));
                        startActivityForResult(photoIntent,IntentCodes.START_CAMERA);
                    }
                };

                return listener;
            }
        };



        return photoListener;
    }


}
