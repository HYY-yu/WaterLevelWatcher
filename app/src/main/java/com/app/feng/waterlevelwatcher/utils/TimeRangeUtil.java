package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;
import android.widget.TextView;

import com.app.feng.waterlevelwatcher.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by feng on 2017/3/23.
 */

public class TimeRangeUtil {

    public static void initDefaultTimeRange(Context context) {
        //如果用户没有修改, 默认显示最近七天的数据
        //如果用户修改了,则每次启动都保证是用户修改的时间段
        if (!checkIsUserEdit(context)) {
            setTimeByRange(context,-7);
        }
    }

    public static void setTimeByRange(Context context,int range) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        TimeRangeUtil.matchCalendar(calendar);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Config.Constant.TIME_FORMAT);

        SharedPref.getInstance(context)
                .putString(Config.KEY.DEFAULT_END_TIME,simpleDateFormat.format(calendar.getTime()));

        calendar.add(Calendar.DAY_OF_MONTH,range);

        SharedPref.getInstance(context)
                .putString(Config.KEY.DEFAULT_START_TIME,
                           simpleDateFormat.format(calendar.getTime()));
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

    public static void showTimeRangeSelector(
            final Context context,TextView tvStartTime,TextView tvEndTime) {

        String startTimeString = (String) tvStartTime.getTag();
        String endTimeString = (String) tvEndTime.getTag();

        //        //  开启 start 时间选择
        //        final TimeSelector timeSelectorStart = new TimeSelector(context,new TimeSelector.ResultHandler() {
        //            @Override
        //            public void handle(String s) {
        //                //在这里 开启 end 时间选择
        //                TimeSelector timeSelectorEnd = new TimeSelector(context,new TimeSelector.ResultHandler() {
        //                    @Override
        //                    public void handle(String s) {
        //
        //                    }
        //                },,);
        //                timeSelectorEnd.setTitle(context.getString(R.string.chioce_end_time_string));
        //                timeSelectorEnd.disScrollUnit(TimeSelector.SCROLLTYPE.MINUTE);
        //                timeSelectorEnd.show();
        //            }
        //        },, );
        //
        //        timeSelectorStart.setTitle(context.getString(R.string.chioce_start_time_stirng));
        //        timeSelectorStart.disScrollUnit(TimeSelector.SCROLLTYPE.MINUTE);
        //        timeSelectorStart.show();
    }
}
