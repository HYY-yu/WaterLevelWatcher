package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

    /**
     * 将calendar修改为最近的偶数时间(向下找), 返回格式 xxxx/xx/xx 02:00
     *
     * @param calendar
     * @return
     */
    public static void matchCalendar(Calendar calendar) {
        int hourDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourDay % 2 != 0) {
            calendar.add(Calendar.HOUR_OF_DAY,-1);
        }
        calendar.set(Calendar.MINUTE,0);
    }

    public static void initDefaultTimeRange(Context context) {
        //如果用户没有修改, 默认显示最近一个月的数据
        //如果用户修改了,则每次启动都保证是用户修改的时间段
        if (!checkIsUserEdit(context)) {
            //当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Utils.matchCalendar(calendar);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Config.Constant.TIME_FORMAT);

            SharedPref.getInstance(context)
                    .putString(Config.KEY.DEFAULT_END_TIME,
                               simpleDateFormat.format(calendar.getTime()));

            calendar.add(Calendar.MONTH,-1);

            SharedPref.getInstance(context)
                    .putString(Config.KEY.DEFAULT_START_TIME,
                               simpleDateFormat.format(calendar.getTime()));
        }

    }

    private static boolean checkIsUserEdit(Context context) {
        return SharedPref.getInstance(context)
                .getBoolean(Config.KEY.USER_EDIT_DEFAULT_TIME,false);
    }
}
