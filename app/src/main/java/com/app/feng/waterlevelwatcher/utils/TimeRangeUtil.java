package com.app.feng.waterlevelwatcher.utils;

import android.content.Context;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.ui.MainActivity;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by feng on 2017/3/23.
 */

public class TimeRangeUtil {

    public static void initDefaultTimeRange(Context context) {
        //如果用户没有修改, 默认显示最近三天的数据
        //如果用户修改了,则每次启动都保证是用户修改的时间段
        if (!checkIsUserEdit(context)) {
            setTimeByRange(context,-3);
        }
    }

    public static void setTimeByRange(Context context,int range) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        TimeRangeUtil.matchCalendar(calendar);

        SharedPref.getInstance(context)
                .putString(Config.KEY.DEFAULT_END_TIME,Utils.format(calendar.getTime()));

        calendar.add(Calendar.DAY_OF_MONTH,range);

        SharedPref.getInstance(context)
                .putString(Config.KEY.DEFAULT_START_TIME,
                           Utils.format(calendar.getTime()));
    }

    private static boolean checkIsUserEdit(Context context) {
        return SharedPref.getInstance(context)
                .getBoolean(Config.KEY.USER_EDIT_DEFAULT_TIME,false);
    }

    /**
     * 将calendar修改为最近的偶数时间(向下找), 返回格式 xxxx/xx/xx 02:00:00
     *
     * @param calendar
     * @return
     */
    public static Calendar matchCalendar(Calendar calendar) {
        int hourDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourDay % 2 != 0) {
            calendar.add(Calendar.HOUR_OF_DAY,-1);
        }
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar;
    }

    public static void showTimeSelector(
            Context context,String startTime,String endTime,
            TimeSelector.ResultHandler resultHandler) {
        //打开TimePicker
        TimeSelector timeSelector = new TimeSelector(context,resultHandler,startTime,endTime);

        timeSelector.disScrollUnit(TimeSelector.SCROLLTYPE.MINUTE);
        timeSelector.show();
    }

    public static void showTimeSelectorByYYYYMMDD(Context context,String startTime,String endTime,
                                                  TimeSelector.ResultHandler resultHandler) {
        //打开TimePicker
        TimeSelector timeSelector = new TimeSelector(context,resultHandler,startTime,endTime);

        timeSelector.disScrollUnit(TimeSelector.SCROLLTYPE.MINUTE);
        timeSelector.disScrollUnit(TimeSelector.SCROLLTYPE.HOUR);
        timeSelector.show();
    }

    public static void showTimeRangeSelector(
            final MainActivity mainActivity) {

        final String dEndTime = SharedPref.getInstance(mainActivity)
                .getString(Config.KEY.DEFAULT_END_TIME,Utils.format(new Date()));

        //  开启 start 时间选择
        final TimeSelector timeSelectorStart = new TimeSelector(mainActivity,
                                                                new TimeSelector.ResultHandler() {
                                                                    @Override
                                                                    public void handle(String s) {
                                                                        mainActivity.setTimeRangeTextStart(
                                                                                s);

                                                                        //在这里 开启 end 时间选择
                                                                        openEndTimeSelector(
                                                                                mainActivity,s,
                                                                                dEndTime);

                                                                    }
                                                                },Config.Constant.GLOBAL_START_TIME,
                                                                dEndTime);

        timeSelectorStart.setTitle(mainActivity.getString(R.string.chioce_start_time_stirng));
        timeSelectorStart.disScrollUnit(TimeSelector.SCROLLTYPE.MINUTE);
        timeSelectorStart.show();
    }

    private static void openEndTimeSelector(
            final MainActivity mainActivity,String startTimeString,String dEndTime) {
        TimeSelector timeSelectorEnd = new TimeSelector(mainActivity,
                                                        new TimeSelector.ResultHandler() {
                                                            @Override
                                                            public void handle(
                                                                    String s) {
                                                                mainActivity.setTimeRangeTextEnd(s);

                                                                //把id设置为-1 表示选择时间范围后必须查询 否则会不查询
                                                                mainActivity.slidePanelEventControlIMPL.queryNewTimeRange();

                                                            }

                                                        },startTimeString,dEndTime);
        timeSelectorEnd.setTitle(mainActivity.getString(R.string.chioce_end_time_string));
        timeSelectorEnd.disScrollUnit(TimeSelector.SCROLLTYPE.MINUTE);
        timeSelectorEnd.show();

    }
}
