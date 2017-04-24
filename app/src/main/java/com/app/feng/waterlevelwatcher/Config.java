package com.app.feng.waterlevelwatcher;

/**
 * Created by feng on 2017/3/8.
 */

public class Config {
    public static String SP_NAME = "waterlevelwatcher";
    public static int MAP_ZOOM_LEVEL = 9;

    public static class Constant {
        public static final String SETTING = "setting";
        public static final String STATISTICS = "statistics";
        public static String MAP = "map";
        public static String OVERVIEW = "overview";

        public static String TIME_FORMAT = "yyyy/MM/dd HH:mm";
        public static String GLOBAL_START_TIME = "2015/12/24 00:00";

    }

    public static class API{
        public static final String baseUrl = "http://10.0.2.2:8080/nsbdserver/";
        public static final String loginUrl = "login";
        public static final String overviewAllStationUrl = "schedule/overviewAllStationByTime";
        public static final String queryOneStationByTimeRange = "schedule/queryOneStationByTimeRange";
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
    }
}
