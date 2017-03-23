package com.app.feng.waterlevelwatcher.application;

import android.app.Application;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.app.feng.waterlevelwatcher.utils.TimeRangeUtil;
import com.app.feng.waterlevelwatcher.utils.Utils;

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

        //确保此方法最后执行
        loadJSON();

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
