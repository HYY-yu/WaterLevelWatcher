package com.app.feng.waterlevelwatcher.bean;

import io.realm.RealmObject;

/**
 * 监测站
 * Created by feng on 2017/3/6.
 */

public class MonitoringStationBean extends RealmObject {
    private int sluiceID;
    private String longitude; // 经度
    private String latitude;  // 纬度

    public int getSluiceID() {
        return sluiceID;
    }

    public void setSluiceID(int sluiceID) {
        this.sluiceID = sluiceID;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
