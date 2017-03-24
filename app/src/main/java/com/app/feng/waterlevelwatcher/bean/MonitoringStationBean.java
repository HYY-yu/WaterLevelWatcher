package com.app.feng.waterlevelwatcher.bean;

import io.realm.RealmObject;

/**
 * 监测站
 * Created by feng on 2017/3/6.
 */

public class MonitoringStationBean extends RealmObject {
    public static final String SLUICEID = "sluiceID";
    public static final String NAME = "name";

    private int sluiceID;
    private String name;
    // 教训: 经纬度不参与计算,完全可以使用String类型,使用float类型会导致舍入问题

    private String longitude; // 经度
    private String latitude;  // 纬度

    public int getSluiceID() {
        return sluiceID;
    }

    public void setSluiceID(int sluiceID) {
        this.sluiceID = sluiceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
