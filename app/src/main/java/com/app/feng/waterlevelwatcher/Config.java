package com.app.feng.waterlevelwatcher;

/**
 * Created by feng on 2017/3/8.
 */

public class Config {

    public static String SP_NAME = "waterlevelwatcher";
    public static boolean LOG_SWITCH = true;
    public static int MAP_ZOOM_LEVEL = 9;

    public static class Constant {
        public static final String SETTING = "setting";
        public static final String STATISTICS = "statistics";
        public static String MAP = "map";
        public static String OVERVIEW = "overview";

        public static String TIME_FORMAT = "yyyy/MM/dd HH:mm";

        //        public enum CHART_YAXIS_TYPE {
        //            OPENING,// 开度
        //            FRONT, // 前水位
        //            BACK // 后水位
        //        }

    }

    public static class KEY {
        public static final String DEFAULT_START_TIME = "DEFAULT_START_TIME";
        public static final String DEFAULT_END_TIME = "DEFAULT_END_TIME";
        public static final String USER_EDIT_DEFAULT_TIME = "USER_EDIT_DEFAULT_TIME";
        public static final String FIRST_RUN = "FIRST_RUN";
        public static final String SHOW_MASK = "SHOW_MASK";
        public static final String ISNIGHT = "ISNIGHT";
        public static final Object CHANGE_DEFAULT_TIME = "CHANGE_DEFAULT_TIME";
        public static final String FIXTABLE_TITLE = "FIXTABLE_TITLE";
        public static final String FIXTABLE_DATA = "FIXTABLE_DATA";
        public static final String FIXTABLE_DATA_COLUMN = "FIXTABLE_DATA_COLUMN";
    }
}
