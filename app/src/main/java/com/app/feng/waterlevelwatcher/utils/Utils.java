package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

/**
 * Created by feng on 2017/3/8.
 */

public class Utils {

    public static List<SluiceBean> fromJson(Context context) {
        Gson gson = new GsonBuilder().create();

        AssetManager assetManager = context.getAssets();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    assetManager.open("result.json"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            List<SluiceBean> tempList = gson.fromJson(reader,
                                                      new TypeToken<List<SluiceBean>>() {}.getType());
            reader.close();

            return tempList;

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void saveToRealm(List<SluiceBean> sluiceBeanList) {
        if (isListEmpty(sluiceBeanList)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(sluiceBeanList);
        realm.commitTransaction();
        realm.close();
    }

    public static <E> boolean isListEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

}
