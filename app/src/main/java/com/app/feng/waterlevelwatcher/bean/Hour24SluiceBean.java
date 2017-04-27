package com.app.feng.waterlevelwatcher.bean;

import java.util.Date;

/**
 * Created by feng on 2017/4/26.
 */
public class Hour24SluiceBean {

    private int jctid;
    private Float frontwl;
    private Date jctime;

    public int getJctid() {
        return jctid;
    }

    public void setJctid(int jctid) {
        this.jctid = jctid;
    }

    public Float getFrontwl() {
        return frontwl;
    }

    public void setFrontwl(Float frontwl) {
        this.frontwl = frontwl;
    }

    public Date getJctime() {
        return jctime;
    }

    public void setJctime(Date jctime) {
        this.jctime = jctime;
    }
}
