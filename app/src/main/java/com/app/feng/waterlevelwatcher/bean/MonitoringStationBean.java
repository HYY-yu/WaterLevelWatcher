package com.app.feng.waterlevelwatcher.bean;

import io.realm.RealmObject;

/**
 * 监测站
 * Created by feng on 2017/3/6.
 */

public class MonitoringStationBean extends RealmObject {
    private int sluiceID;
    private float longitude; // 经度
    private float latitude;  // 纬度

    public int getSluiceID() {
        return sluiceID;
    }

    public void setSluiceID(int sluiceID) {
        this.sluiceID = sluiceID;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
