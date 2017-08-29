package com.app.feng.waterlevelwatcher.bean;

import io.realm.RealmObject;

/**
 * Created by feng on 2017/4/27.
 */

public class FS_StatisticsBean extends RealmObject{

    public static final String DRID = "drid";

    private String drid;
    private String fsknm;
    private Float flow;
    private Float curflow;
    private Float totalflow;

    public static String[] getTitles() {
        return new String[]{"分水口门","瞬时流量(m3/s)","当日分水(m3)","累计分水(万m3)"};
    }

    public String getDrid() {
        return drid;
    }

    public void setDrid(String drid) {
        this.drid = drid;
    }

    public String getFsknm() {
        return fsknm;
    }

    public void setFsknm(String fsknm) {
        this.fsknm = fsknm;
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
}
