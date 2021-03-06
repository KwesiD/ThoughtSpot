package com.example.kwesi.thoughtspot;

import android.Manifest;
import android.app.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.support.v4.app.DialogFragment;

/**
 * Created by Kwesi on 7/5/2018.
 */


/**
 * This activity is what the user sees when they try and add a new activity/event to their list.
 * If the user chooses to edit an activity, they are brought to this same page, but with the fields
 * pre-filled.
 */
public class EditActivitiesActivity extends AppCompatActivity implements AddTaskDialog.AddTaskDialogListener{
    //private Activity activity; //The activity object
    private ArrayList<Uri> imageArray = new ArrayList<Uri>(); //TODO: Temporary until proper image methods are in place
    private ArrayList<String> tagArray = new ArrayList<String >(); //TODO: Also Temp
    private Uri currImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity_layout);

        //the intent that called this activity
        Intent caller = getIntent();

        //checks for request code. If DNE, default to New Activity
        int requestCode = caller.getIntExtra("requestCode", Codes.NEW_ACTIVITY);

        Button addPhotos = findViewById(R.id.edit_add_photos_button);
        addPhotos.setOnClickListener(addPhotoListener()); //Add a listener to the add photos button

        Button addTags = findViewById(R.id.edit_add_tags_button);
        addTags.setOnClickListener(addTagsListener());

        if(requestCode == Codes.NEW_ACTIVITY){ //If we are creating new activity
            Button submitButton = findViewById(R.id.edit_activity_submit);
            submitButton.setOnClickListener(submitListener());
        }
        else if(requestCode == Codes.EDIT_ACTIVITY){ //If we are editing an old activity
            //TODO: Handle edit activity
        }
    }

//    private Activity createNewActivity(String name,String location,String description,String min,String max,ArrayList<Bitmap> photos,ArrayList<String> tags){
//        Activity newActivity = new Activity(name,location,description,min,max,photos,tags);
//        return newActivity;
//    }

    private ArrayList<Uri> getPhotos(){
        return imageArray;
    }

    private ArrayList<String> getTags(GridLayout tagGrid){
        return tagArray;
    }

    private void addTag(String tag){
        tagArray.add(tag);
        LayoutInflater inflater = getLayoutInflater();
        View tagView = inflater.inflate(R.layout.tag_layout,null);
        TextView tagText = tagView.findViewById(R.id.tag_text);
        tagText.setText(tag);
        GridLayout tagGrid = findViewById(R.id.edit_tag_grid);
        tagGrid.addView(tagView);

    }

    /**
     * The submit button checks if all required fields are filled before proceeding.
     * Sets the parameters for the user's activity before returning it to the main activity.
     * @return - A listener for the submit button
     */
    private View.OnClickListener submitListener(){
        final View.OnClickListener submitListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Required fields
                String name = ((EditText)findViewById(R.id.edit_activity_name)).getText().toString().trim();
                String location = ((EditText)findViewById(R.id.edit_activity_location)).getText().toString().trim();

                if(name.equals("")){
                   Toast.makeText(view.getContext(),"Activity Name Required",Toast.LENGTH_SHORT).show();
                }
                else if(location.equals("")){
                    Toast.makeText(view.getContext(),"Activity Location Required",Toast.LENGTH_SHORT).show();
                }
                else if(name.equals("") && location.equals("")){
                    Toast.makeText(view.getContext(),"Activity Name and Activity Location Required",Toast.LENGTH_SHORT).show();
                }
                else {
                    String description = ((EditText) findViewById(R.id.edit_activity_description)).getText().toString().trim();
                    String min = ((EditText) findViewById(R.id.price_range_min)).getText().toString().trim(); //TODO: Throw error + toast if min >= max
                    String max = ((EditText) findViewById(R.id.price_range_max)).getText().toString().trim(); //TODO: Change min/max to ints
                    ArrayList<Uri> photos = getPhotos();
                    ArrayList<String> tags = getTags((GridLayout) findViewById(R.id.edit_tag_grid));
                    Intent returnIntent = new Intent();
                    com.example.kwesi.thoughtspot.Activity activity = new com.example.kwesi.thoughtspot.Activity(name,location,description,min,max,photos,tags);
                    /*returnIntent.putExtra("name", name);
                    returnIntent.putExtra("location", location);
                    returnIntent.putExtra("description", description);
                    returnIntent.putExtra("min", min);
                    returnIntent.putExtra("max", max);
                    //returnIntent.putExtra("photos", photos);
                    returnIntent.putExtra("tags", tags);*/
                    returnIntent.putExtra("activity",activity);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
          }
        };

        return submitListener;
    }

    /**
     * Creates a listener for the add photo button. Manages the opening of the camera/gallery to upload photos.
     * @return - A listener for the add photo button
     */
    private View.OnClickListener addPhotoListener(){
        View.OnClickListener photoListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(view);
            }

            /**
             * Opens a dialog containing the gallery and camera. User can choose how to
             * upload a photo from here.
             * @param view - Active View
             */
            private void openDialog(View view){
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Upload Photo")
                        .setMessage("How do you want to upload a photo?")
                        .setPositiveButton("Camera",cameraListener(view));
                dialog.show();
            }

            /**
             * Sends a camera intent to upload photos
             * @param view - Active View
             * @return - Returns a listener for the camera option in the dialog box
             */
            private DialogInterface.OnClickListener cameraListener(final View view){
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { //double check permissions before sending intent
                            requestPermissions(new String[]{Manifest.permission.CAMERA},Codes.CAMERA_REQUEST_CODE);
                        }
                        File photoFile = null;
                        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        try{
                            photoFile = createImage();
                            Uri photoUri = null;
                            if(photoFile != null){
                                photoUri = FileProvider.getUriForFile(view.getContext(),"com.example.kwesi.thoughtspot.fileprovider",photoFile);
                                currImageUri = photoUri;
                                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                                startActivityForResult(photoIntent, Codes.START_CAMERA);
                            }
                            else{
                                throw new IOException("photoFile is null");
                            }
                        }
                        catch (IOException io){
                            io.printStackTrace();
                            Toast.makeText(view.getContext(),"Error while creating file",Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                return listener;
            }
        };

        return photoListener;
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this,currImagePath.toString(),Toast.LENGTH_SHORT).show();
        switch(requestCode){
            case Codes.START_CAMERA:
                cameraResultHandler(resultCode,data);
                break;
        }
    }

    /**
     * Handles creating the thumbnail and saving photo from the camera.
     * @param resultCode - Result code from the intent
     * @param data - The data from the intent
     */
    private void cameraResultHandler(int resultCode,Intent data){
        if(resultCode == RESULT_OK){
            try{

                //Bundle extras = data.getExtras();
                //Log.e("ResultHandler",extras.toString());
                //Bitmap image = (Bitmap) (extras.get("data")); //Get image bitmap

                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(),currImageUri);
                //Inflate the layout for a thumbnail button
                View thumbnailView;
                LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                thumbnailView = inflater.inflate(R.layout.edit_photo_button,null);
                ImageView thumbnail = thumbnailView.findViewById(R.id.thumbnail);
                thumbnail.setImageBitmap(image);

                LinearLayout photoScroller = findViewById(R.id.edit_photo_scroller); //Horizontal scrolling view for thumbnails
                photoScroller.addView(thumbnailView); //sets thumbnail as a child for the scrolling view

                imageArray.add(currImageUri);
                //Temporary ^^
            }
            catch (Exception e){
                //Toast.makeText(this,"Could not upload photo",Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }

    }

    private File createImage() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName,".jpg",storageDir);
        return image;
    }

    private View.OnClickListener addTagsListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new AddTaskDialog();
                dialog.show(getSupportFragmentManager(),"AddTaskDialog");
            }
        };
        return listener;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        addTag(((AddTaskDialog)dialog).getCurrTag());
    }

   /* @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }*/
}