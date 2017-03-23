package com.app.feng.waterlevelwatcher.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;

import io.realm.Realm;

/**
 * Created by feng on 2017/3/22.
 */

public class DialogUtil {

    public static void showEditPositionDialog(
            Context context,String oldLongitude,String oldLatitude,final Realm realm,
            final int sluiceID) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.dialog_edit_position,null);
        final EditText et_longitude = (EditText) v.findViewById(R.id.editText_longitude);
        final EditText et_latitude = (EditText) v.findViewById(R.id.editText_latitude);
        et_longitude.setText(oldLongitude);
        et_latitude.setText(oldLatitude);

        new AlertDialog.Builder(context).setTitle("修改位置")
                .setView(v)
                .setCancelable(true)
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        //设置新位置
                        MonitoringStationBean b = RealmUtil.loadStationDataById(realm,sluiceID);
                        realm.beginTransaction();
                        b.setLongitude(et_longitude.getText()
                                               .toString());
                        b.setLatitude(et_latitude.getText()
                                              .toString());
                        realm.commitTransaction();

                        //TODO: 是否需要通知服务器修改位置呢?
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
