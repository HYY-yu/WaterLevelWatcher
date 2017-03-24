package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.feng.waterlevelwatcher.Config;

/**
 *
 *   *****  注意: 此单例非线程安全 *****
 * Created by wanglei on 2016/11/27.
 */

public class SharedPref {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    static final String SP_NAME = Config.SP_NAME;

    private static SharedPref instance;

    private SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPref(context.getApplicationContext());
        }
        return instance;
    }

    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public void clear() {
        editor.clear()
                .apply();
    }

    public void putInt(String key,int value) {
        editor.putInt(key,value);
        editor.apply();
    }

    public int getInt(String key,int defValue) {
        return sharedPreferences.getInt(key,defValue);
    }

    public void putLong(String key,Long value) {
        editor.putLong(key,value);
        editor.apply();
    }

    public long getLong(String key,long defValue) {
        return sharedPreferences.getLong(key,defValue);
    }


    public void putBoolean(String key,Boolean value) {
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean getBoolean(String key,boolean defValue) {
        return sharedPreferences.getBoolean(key,defValue);
    }


    public void putString(String key,String value) {
        editor.putString(key,value);
        editor.apply();
    }

    public String getString(String key,String defValue) {
        return sharedPreferences.getString(key,defValue);
    }

}
