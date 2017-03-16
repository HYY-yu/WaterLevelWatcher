package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

/**
 * Created by feng on 2017/3/8.
 */

public class Utils {

    public static int LODD_JSON_MODE_WATERLEVEL = 1;
    public static int LODD_JSON_MODE_STATION = 2;

    public static <T> List<T> fromJson(Context context,String jsonPath,int mode) {
        Gson gson = new GsonBuilder().create();

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

}
