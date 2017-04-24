package com.app.feng.waterlevelwatcher.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.feezu.liuli.timeselector.Utils.DateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by feng on 2017/3/8.
 */

public class Utils {

    public static int LODD_JSON_MODE_WATERLEVEL = 1;
    public static int LODD_JSON_MODE_STATION = 2;

    public static <T> List<T> fromJson(Context context,String jsonPath,int mode) {
        Gson gson = new GsonBuilder().setDateFormat(Config.Constant.TIME_FORMAT).create();

        AssetManager assetManager = context.getAssets();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(assetManager.open(jsonPath)))) {
            if (mode == LODD_JSON_MODE_WATERLEVEL) {
                return gson.<List>fromJson(reader,new TypeToken<List<SluiceBean>>() {}.getType());
            } else {
                return gson.<List>fromJson(reader,
                                           new TypeToken<List<MonitoringStationBean>>() {}.getType());

            }

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static <E> boolean isListEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

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

    public static Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);

        return view.getDrawingCache();
    }

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static Date parse(String time) {
        return DateUtil.parse(time,Config.Constant.TIME_FORMAT);
    }

    public static String format(Date time) {
        return DateUtil.format(time,Config.Constant.TIME_FORMAT);
    }
}
