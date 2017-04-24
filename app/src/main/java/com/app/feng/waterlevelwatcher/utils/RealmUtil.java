package com.app.feng.waterlevelwatcher.utils;

import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.app.feng.waterlevelwatcher.utils.Utils.isListEmpty;

/**
 *
 * 数据库的操作集合
 * Created by feng on 2017/3/10.
 */

public class RealmUtil {

    public static RealmResults<SluiceBean> loadDataByTime(Realm realm,Date time) {
        return realm.where(SluiceBean.class)
                .equalTo(SluiceBean.TIME,time)
                .findAllSorted(MonitoringStationBean.SLUICEID,Sort.ASCENDING);
    }

    public static RealmResults<SluiceBean> loadDataById(Realm realm,int sluiceID) {
        return realm.where(SluiceBean.class)
                .equalTo(SluiceBean.SLUICEID,sluiceID)
                .findAllSorted(SluiceBean.TIME,Sort.ASCENDING);
    }

    public static RealmResults<SluiceBean> loadDataByIdAndTimeRange(Realm realm,int sluiceID,
                                                                    String startTimeString,String
                                                                            endTimeString) {
        Date startTime = Utils.parse(startTimeString);
        Date endTime = Utils.parse(endTimeString);
        return realm.where(SluiceBean.class)
                .equalTo(SluiceBean.SLUICEID,sluiceID)
                .between(SluiceBean.TIME,startTime,endTime)
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
