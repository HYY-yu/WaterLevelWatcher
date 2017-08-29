package com.app.feng.waterlevelwatcher.bean;

import java.util.Date;

import io.realm.RealmObject;

/**
 * 水闸数据
 * Created by feng on 2017/3/2.
 */

public class SluiceBean extends RealmObject {
    public static final String SLUICEOPENING1 = "sluiceOpening1";
    public static final String TIME = "time";
    public static final String SLUICEID = "sluiceID";


    private int sluiceID;         //闸编号
    private float sluiceOpening1;   //闸门开度
    private float sluiceOpening2;   //闸门开度
    private float sluiceOpening3;   //闸门开度
    private float sluiceOpening4;   //闸门开度
    private float waterLevel_front; //闸前水位
    private float waterLevel_back;  //闸后水位

    private Date time;   // 时间

    public int getSluiceID() {
        return sluiceID;
    }

    public void setSluiceID(int sluiceID) {
        this.sluiceID = sluiceID;
    }

    public float getSluiceOpening1() {
        return sluiceOpening1;
    }

    public void setSluiceOpening1(float sluiceOpening1) {
        this.sluiceOpening1 = sluiceOpening1;
    }

    public float getWaterLevel_front() {
        return waterLevel_front;
    }

    public void setWaterLevel_front(float waterLevel_front) {
        this.waterLevel_front = waterLevel_front;
    }

    public float getWaterLevel_back() {
        return waterLevel_back;
    }

    public void setWaterLevel_back(float waterLevel_back) {
        this.waterLevel_back = waterLevel_back;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getSluiceOpening2() {
        return sluiceOpening2;
    }

    public void setSluiceOpening2(float sluiceOpening2) {
        this.sluiceOpening2 = sluiceOpening2;
    }

    public float getSluiceOpening3() {
        return sluiceOpening3;
    }

    public void setSluiceOpening3(float sluiceOpening3) {
        this.sluiceOpening3 = sluiceOpening3;
    }

    public float getSluiceOpening4() {
        return sluiceOpening4;
    }

    public void setSluiceOpening4(float sluiceOpening4) {
        this.sluiceOpening4 = sluiceOpening4;
    }

    @Override
    public String toString() {
        return "SluiceBean{" + "sluiceID=" + sluiceID + ", sluiceOpening1=" + sluiceOpening1 + ", sluiceOpening2=" + sluiceOpening2 + ", sluiceOpening3=" + sluiceOpening3 + ", sluiceOpening4=" + sluiceOpening4 + ", waterLevel_front=" + waterLevel_front + ", waterLevel_back=" + waterLevel_back + ", time=" + time + '}';
    }
}
