package com.app.feng.waterlevelwatcher.bean;

import com.app.feng.waterlevelwatcher.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;

/**
 * 水闸数据
 * Created by feng on 2017/3/2.
 */

public class SluiceBean extends RealmObject{
    private int sluiceID;         //闸编号
    private float sluiceOpening;   //闸门开度
    private float waterLevel_front; //闸前水位
    private float waterLevel_back;  //闸后水位

    private Date time;   // 时间
    private String timeFormatString;

    public int getSluiceID() {
        return sluiceID;
    }

    public void setSluiceID(int sluiceID) {
        this.sluiceID = sluiceID;
    }

    public float getSluiceOpening() {
        return sluiceOpening;
    }

    public void setSluiceOpening(float sluiceOpening) {
        this.sluiceOpening = sluiceOpening;
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
        timeFormatString = (new SimpleDateFormat(Config.Constant.TIME_FORMAT)).format(time);
    }

    public String getFormatTime() {
        return timeFormatString;
    }

}
