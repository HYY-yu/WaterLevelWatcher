package com.app.feng.waterlevelwatcher;

/**
 * Created by feng on 2017/3/8.
 */

public class Config {

    public static String SP_NAME = "waterlevelwatcher";
    public static boolean LOG_SWITCH = true;

    public static class Constant {
        public static String MAP = "map";
        public static String OVERVIEW = "overview";

        public static String TIME_FORMAT = "yyyy/MM/dd HH:mm";

        public enum CHART_YAXIS_TYPE {
            OPENING ,// 开度
            FRONT, // 前水位
            BACK // 后水位
        }

    }

    public static class KEY {
        public static String JSON_LOAD = "JSON_LOAD";
    }
}
