package com.app.feng.waterlevelwatcher.utils.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;

import com.app.feng.waterlevelwatcher.R;

/**
 * Created by feng on 2017/4/27.
 */

public class LoadingDialogManager {
    public static Dialog openDialog(Activity activity) {
        Dialog dialog;
        View loading = LayoutInflater.from(activity)
                .inflate(R.layout.loading_view,null);
        dialog = new AlertDialog.Builder(activity).setTitle("网络加载")
                .setView(loading)
                .setCancelable(false)
                .create();
        dialog.show();

        return dialog;
    }

    public static void closeDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
