package com.app.feng.waterlevelwatcher.utils;

import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by feng on 2017/3/10.
 */

public class RealmUtils {

    public static RealmResults<SluiceBean> loadDataByTime(Realm realm,String time) {
        return realm.where(SluiceBean.class)
                .equalTo("timeFormatString",time)
                .findAllSorted("sluiceID",Sort.ASCENDING);
    }

    public static RealmResults<SluiceBean> loadDataById(Realm realm,int sluiceID) {
        return realm.where(SluiceBean.class)
                .equalTo("sluiceID",sluiceID)
                .findAllSorted("time",Sort.ASCENDING);
    }

    public static MonitoringStationBean loadStationDataById(Realm realm,int sluiceID){
        return realm.where(MonitoringStationBean.class)
                .equalTo("sluiceID",sluiceID)
                .findFirst();
    }
}
