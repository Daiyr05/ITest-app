package com.example.itest.alertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.itest.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;
    private  AlertDialog.Builder builder;
    public LoadingDialog(Activity activity){

        builder = new AlertDialog.Builder(activity);
        this.activity = activity;
    }

    public void startLoadingDiaolog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custome_alert_dialog,null));
        dialog = builder.create();
        dialog.show();
    }
    public void setCancelable(boolean bl){
        builder.setCancelable(bl);
    }

    public void dismissDiaolog(){
        dialog.dismiss();
    }
}
