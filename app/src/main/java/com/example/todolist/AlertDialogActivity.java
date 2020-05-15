package com.example.todolist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;


public class AlertDialogActivity extends AppCompatDialogFragment {

    private EditText editText;
    private AlertDialogInterface alertDialogInterface;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        androidx.appcompat.app.AlertDialog.Builder builder = new  androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.alertdialog_label, null);
        builder.setView(view).setTitle("Add a Custom Label").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String label = editText.getText().toString();
                alertDialogInterface.applyTexts(label);
            }
        });
        editText = view.findViewById(R.id.dialogEdit);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            alertDialogInterface = (AlertDialogInterface) context;
        } catch (Exception e) {
           throw new ClassCastException(context.toString());
        }
    }

    public interface AlertDialogInterface{
        void applyTexts(String label);
    }
}
