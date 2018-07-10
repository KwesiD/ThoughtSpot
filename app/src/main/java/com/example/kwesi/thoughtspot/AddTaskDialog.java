package com.example.kwesi.thoughtspot;

import android.app.*;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Kwesi on 7/8/2018.
 */

public class AddTaskDialog extends DialogFragment {

    private AddTaskDialogListener dialogListener;
    private String currTag;

    public interface AddTaskDialogListener{
        void onDialogPositiveClick(DialogFragment dialog);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add A New Tag");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_tag_popup,null);
        EditText editText = dialogLayout.findViewById(R.id.add_tag_text_field);
        builder.setView(dialogLayout)
            .setPositiveButton("Add New Tag",addNewTagListener(editText))
            .setNegativeButton("Cancel",cancelListener());
        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            dialogListener = (AddTaskDialogListener) context;
        }
        catch (ClassCastException cc){
            cc.printStackTrace();
        }

    }

    private DialogInterface.OnClickListener addNewTagListener(final EditText editText){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currTag = editText.getText().toString();
                dialogListener.onDialogPositiveClick(AddTaskDialog.this);
            }
        };

        return listener;
    }

    private DialogInterface.OnClickListener cancelListener(){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        };

        return listener;
    }

    public String getCurrTag(){
        return currTag;
    }
}
