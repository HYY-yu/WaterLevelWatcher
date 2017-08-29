package com.app.feng.waterlevelwatcher.bean;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by feng on 2017/4/26.
 */
public class AllLineWaterDepthBean extends RealmObject {
    //jctid numeric(18, 0),
    //jctname varchar(100),		--节制闸名称
    //fronttopelev float,			--闸前渠底高程(m)
    //backtopelev float,			--闸后渠底高程(m)
    //designstage float,			--设计水位(m)
    //frontwl float,				--闸前水位(m)
    //backwl float,				--闸后水位(m)
    //frontchannelwl float,		--闸前渠道水深(m)
    //backchannelwl float,		--闸后渠道水深(m)
    //todesignstage float			--当前水位与设计水位差(m)

    private int jctid;
    private String jctname;
    private Float fronttopelev;
    private Float backtopelev;
    private Float designstage;
    private Float frontwl;
    private Float backwl;
    private Float frontchannelwl;
    private Float backchannelwl;
    private Float todesignstage;

    public static String[] getTitles() {
        return new String[]{"节制闸名称","闸前渠底高程(m)","闸后渠底高程(m)","设计水位(m)","闸前水位(m)","闸后水位(m)","闸前渠道水深(m)",
                            "闸后渠道水深(m)","当前水位与设计水位差"};
    }

    public Date queryDate;

    public int getJctid() {
        return jctid;
    }

    public void setJctid(int jctid) {
        this.jctid = jctid;
    }

    public String getJctname() {
        return jctname;
    }

    public void setJctname(String jctname) {
        this.jctname = jctname;
    }

    public Float getFronttopelev() {
        return fronttopelev;
    }

    public void setFronttopelev(Float fronttopelev) {
        this.fronttopelev = fronttopelev;
    }

    public Float getBacktopelev() {
        return backtopelev;
    }

    public void setBacktopelev(Float backtopelev) {
        this.backtopelev = backtopelev;
    }

    public Float getDesignstage() {
        return designstage;
    }

    public void setDesignstage(Float designstage) {
        this.designstage = designstage;
    }

    public Float getFrontwl() {
        return frontwl;
    }

    public void setFrontwl(Float frontwl) {
        this.frontwl = frontwl;
    }

    public Float getBackwl() {
        return backwl;
    }

    public void setBackwl(Float backwl) {
        this.backwl = backwl;
    }

    public Float getFrontchannelwl() {
        return frontchannelwl;
    }

    public void setFrontchannelwl(Float frontchannelwl) {
        this.frontchannelwl = frontchannelwl;
    }

    public Float getBackchannelwl() {
        return backchannelwl;
    }

    public void setBackchannelwl(Float backchannelwl) {
        this.backchannelwl = backchannelwl;
    }

    public Float getTodesignstage() {
        return todesignstage;
    }

    public void setTodesignstage(Float todesignstage) {
        this.todesignstage = todesignstage;
    }
}
