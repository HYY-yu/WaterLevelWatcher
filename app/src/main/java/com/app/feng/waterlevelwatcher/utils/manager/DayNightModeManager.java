package com.app.feng.waterlevelwatcher.utils.manager;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.ui.MainActivity;
import com.app.feng.waterlevelwatcher.utils.SharedPref;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by feng on 2017/3/25.
 */

public class DayNightModeManager {

    public static void setDayMode(Context context) {
        SharedPref.getInstance(context)
                .putBoolean(Config.KEY.ISNIGHT,false);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if(context instanceof MainActivity){
            EventBus.getDefault().post(false);
        }
    }

    public static void setNightMode(Context context) {
        SharedPref.getInstance(context)
                .putBoolean(Config.KEY.ISNIGHT,true);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //通知 Map 改变模式
        if(context instanceof MainActivity){
            EventBus.getDefault().post(true);
        }
    }
}
