package com.app.feng.waterlevelwatcher.application;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.app.feng.waterlevelwatcher.utils.TimeRangeUtil;
import com.app.feng.waterlevelwatcher.utils.Utils;
import com.app.feng.waterlevelwatcher.utils.manager.DayNightModeManager;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by feng on 2017/3/8.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initRealm();

        TimeRangeUtil.initDefaultTimeRange(getApplicationContext());

        initDayNight();
        //确保此方法最后执行
        loadJSON();


    }

    private void initDayNight() {
        if (!checkDayNightExist()) {
            // 不存在 默认白天
            SharedPref.getInstance(getApplicationContext()).putBoolean(Config.KEY.ISNIGHT,false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            boolean isNight = SharedPref.getInstance(getApplicationContext())
                    .getBoolean(Config.KEY.ISNIGHT,false);
            if(!isNight){
                DayNightModeManager.setDayMode(getApplicationContext());
            }else{
                DayNightModeManager.setNightMode(getApplicationContext());
            }
        }
    }

    private boolean checkDayNightExist() {
        return SharedPref.getInstance(getBaseContext())
                .contains(Config.KEY.ISNIGHT);
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("database.realm")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }


    private boolean checkDataExist() {
        return SharedPref.getInstance(getBaseContext())
                .contains(Config.KEY.FIRST_RUN);
    }

    //第一次进入时加载JSON到数据库
    private void loadJSON() {
        if (!checkDataExist()) {
            List<SluiceBean> sluiceBeans = Utils.fromJson(getBaseContext(),"waterlevel.json",
                                                          Utils.LODD_JSON_MODE_WATERLEVEL);
            RealmUtil.saveToRealm(sluiceBeans);

            List<MonitoringStationBean> beanList = Utils.fromJson(getBaseContext(),"station.json",
                                                                  Utils.LODD_JSON_MODE_STATION);
            RealmUtil.saveToRealm(beanList);

            SharedPref.getInstance(getBaseContext())
                    .putBoolean(Config.KEY.FIRST_RUN,true);
        }
    }
}
