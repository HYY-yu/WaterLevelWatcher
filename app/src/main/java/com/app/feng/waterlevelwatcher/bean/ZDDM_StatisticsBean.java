package com.app.feng.waterlevelwatcher.bean;

import io.realm.RealmObject;

/**
 * Created by feng on 2017/4/27.
 */
public class ZDDM_StatisticsBean extends RealmObject{

    private String drid;
    private int jctid;
    private Float flow;
    private Float curflow;
    private Float totalflow;

    public String jctName;

    public String getDrid() {
        return drid;
    }

    public void setDrid(String drid) {
        this.drid = drid;
    }

    public int getJctid() {
        return jctid;
    }

    public void setJctid(int jctid) {
        this.jctid = jctid;
    }

    public Float getFlow() {
        return flow;
    }

    public void setFlow(Float flow) {
        this.flow = flow;
    }

    public Float getCurflow() {
        return curflow;
    }

    public void setCurflow(Float curflow) {
        this.curflow = curflow;
    }

    public Float getTotalflow() {
        return totalflow;
    }

    public void setTotalflow(Float totalflow) {
        this.totalflow = totalflow;
    }

    public static String[] getTitles() {
        return new String[]{
                "断面名称","瞬时流量","日供水量","累计供水"
        };
    }
}
