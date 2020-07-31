package edu.csumb.flightapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Objects;

public class E_Dialog extends AppCompatDialogFragment {
    String message;
    int tries;
    int count;

    public E_Dialog(String msg, int trs) {
        this.message = msg;
        this.tries = trs;
        this.count = 0;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                count++;
                if(count>=tries){
                    getActivity().finish();
                }
            }
        });
        return builder.create();
    }
}
