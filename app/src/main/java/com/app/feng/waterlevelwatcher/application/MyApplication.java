package com.app.feng.waterlevelwatcher.application;

import android.app.Application;

import com.amap.api.maps2d.AMap;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.utils.RealmUtils;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
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

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("database.realm")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        loadJSON();

    }


    private boolean checkDataExist() {
        return SharedPref.getInstance(getBaseContext())
                .contains(Config.KEY.JSON_LOAD);
    }

    //第一次进入时加载JSON到数据库
    private void loadJSON() {
        if (!checkDataExist()) {
            List<SluiceBean> sluiceBeans = Utils.fromJson(getBaseContext(),"waterlevel.json",
                                                          Utils.LODD_JSON_MODE_WATERLEVEL);
            RealmUtils.saveToRealm(sluiceBeans);

            List<MonitoringStationBean> beanList = Utils.fromJson(getBaseContext(),"station.json",
                                                                  Utils.LODD_JSON_MODE_STATION);
            RealmUtils.saveToRealm(beanList);

            SharedPref.getInstance(getBaseContext())
                    .putBoolean(Config.KEY.JSON_LOAD,true);
        }
    }
}
