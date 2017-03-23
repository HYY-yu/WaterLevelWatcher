package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;

import com.app.feng.waterlevelwatcher.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by feng on 2017/3/23.
 */

public class TimeRangeUtil {

    public static void initDefaultTimeRange(Context context) {
        //如果用户没有修改, 默认显示最近一个月的数据
        //如果用户修改了,则每次启动都保证是用户修改的时间段
        if (!checkIsUserEdit(context)) {
            //当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            TimeRangeUtil.matchCalendar(calendar);

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
}
