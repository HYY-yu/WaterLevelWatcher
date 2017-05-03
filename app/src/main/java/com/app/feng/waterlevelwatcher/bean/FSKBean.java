package com.app.feng.waterlevelwatcher.bean;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by feng on 2017/5/2.
 */
public class FSKBean extends RealmObject{

    public static final String JCTIME = "jctime";

    long jctid;
    Float hole1kd;
    Float instantflow1;
    Float totalflow;

    Date jctime;

    public long getJctid() {
        return jctid;
    }

    public void setJctid(long jctid) {
        this.jctid = jctid;
    }

    public Float getHole1kd() {
        return hole1kd;
    }

    public void setHole1kd(Float hole1kd) {
        this.hole1kd = hole1kd;
    }

    public Float getInstantflow1() {
        return instantflow1;
    }

    public void setInstantflow1(Float instantflow1) {
        this.instantflow1 = instantflow1;
    }

    public Float getTotalflow() {
        return totalflow;
    }

    public void setTotalflow(Float totalflow) {
        this.totalflow = totalflow;
    }

    public Date getJctime() {
        return jctime;
    }

    public void setJctime(Date jctime) {
        this.jctime = jctime;
    }
}
