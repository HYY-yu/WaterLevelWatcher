package com.app.feng.waterlevelwatcher.utils;

import com.app.feng.waterlevelwatcher.bean.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.app.feng.waterlevelwatcher.utils.Utils.isListEmpty;

/**
 * 数据库的操作集合
 * Created by feng on 2017/3/10.
 */

public class RealmUtil {

    public static RealmResults<SluiceBean> loadDataByTime(Realm realm,Date time) {
        return realm.where(SluiceBean.class)
                .equalTo(SluiceBean.TIME,time)
                .findAllSorted(MonitoringStationBean.SLUICEID,Sort.ASCENDING);
    }

    public static RealmResults<FSKBean> loadFSKByTime(Realm realm,Date time) {
        return realm.where(FSKBean.class)
                .equalTo(FSKBean.JCTIME,time)
                .findAll();
    }

    public static RealmResults<SluiceBean> loadDataById(Realm realm,int sluiceID) {
        return realm.where(SluiceBean.class)
                .equalTo(SluiceBean.SLUICEID,sluiceID)
                .findAllSorted(SluiceBean.TIME,Sort.ASCENDING);
    }

    public static RealmResults<SluiceBean> loadDataByIdAndTimeRange(
            Realm realm,int sluiceID,String startTimeString,String endTimeString) {
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

    public static UserBean loadUser(Realm realm) {
        return realm.where(UserBean.class)
                .findFirst();
    }

    public static RealmResults loadAllAllLineBeanByTime(
            Class clazz,
            Realm realm,Date time) {

        return realm.where(clazz)
                .equalTo(AllLineSchedulingAnalysisBean.QUERYDATE,time)
                .findAllSorted(AllLineSchedulingAnalysisBean.JCTID,Sort.ASCENDING);
    }

    public static RealmResults loadAllAllLineBeanByTimeString(Class clazz,Realm realm,String time) {
        return realm.where(clazz)
                .equalTo(FS_StatisticsBean.DRID,changeTimeType(time))
                .findAll();
    }

    private static String changeTimeType(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = dateFormat.parse(time);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");

            return dateFormat1.format(date);
        } catch (ParseException e) {
            return "";
        }
    }
}
