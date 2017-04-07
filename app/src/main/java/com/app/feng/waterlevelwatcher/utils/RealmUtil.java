package com.app.feng.waterlevelwatcher.utils;

import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.app.feng.waterlevelwatcher.utils.Utils.isListEmpty;

/**
 * Created by feng on 2017/3/10.
 */

public class RealmUtil {

    public static RealmResults<SluiceBean> loadDataByTime(Realm realm,String time) {
        return realm.where(SluiceBean.class)
                .equalTo(SluiceBean.TIMEFORMATSTRING,time)
                .findAllSorted(MonitoringStationBean.SLUICEID,Sort.ASCENDING);
    }

    public static RealmResults<SluiceBean> loadDataById(Realm realm,int sluiceID) {
        return realm.where(SluiceBean.class)
                .equalTo(MonitoringStationBean.SLUICEID,sluiceID)
                .findAllSorted(SluiceBean.TIME,Sort.ASCENDING);
    }

    public static MonitoringStationBean loadStationDataById(Realm realm,int sluiceID) {
        return realm.where(MonitoringStationBean.class)
                .equalTo(MonitoringStationBean.SLUICEID,sluiceID)
                .findFirst();
    }

    public static RealmResults<MonitoringStationBean> queryStationByName(
            Realm realm,String name) {
        return realm.where(MonitoringStationBean.class)
                .contains(MonitoringStationBean.NAME,name)
                .findAll();
    }


    public static RealmResults<MonitoringStationBean> loadAllStation(Realm realm) {
        return realm.where(MonitoringStationBean.class)
                .findAllSorted(MonitoringStationBean.SLUICEID,Sort.ASCENDING);
    }

    public static void saveToRealm(List sluiceBeanList) {
        if (isListEmpty(sluiceBeanList)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(sluiceBeanList);
        realm.commitTransaction();
        realm.close();
    }
}
