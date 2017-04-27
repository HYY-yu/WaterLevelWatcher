package com.app.feng.waterlevelwatcher.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmObject;

/**
 * 全线调度辅助分析
 * Created by feng on 2017/4/25.
 */

public class AllLineSchedulingAnalysisBean extends RealmObject implements Parcelable {

    public static final String JCTID = "jctid";
    public static final String QUERYDATE = "queryDate";

    /*
    Pro_AllLineSchedulingAnalysisNew
    jctid int,					--节点编号
    jctname varchar(100),		--节制闸名称
    objectstage float,			--目标水位(m)
    designstage float,			--设计水位(m)
    currentstage float,			--当前水位(m)
    toobjectstage float,		--距目标水位(m)
    todesignstage float,		--距设计水位(m)
    rose2 float,				--2h涨幅(m)
    rose24 float,				--24h涨幅(m)
    trend24 float				--24h涨幅(m)*/

    private int jctid;
    private String jctname;
    private Float objectstage;
    private Float designstage;
    private Float currentstage;
    private Float toobjectstage;
    private Float todesignstage;
    private Float rose2;
    private Float rose24;
    private Float trend24;

    private Date queryDate;

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

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

    public Float getObjectstage() {
        return objectstage;
    }

    public void setObjectstage(Float objectstage) {
        this.objectstage = objectstage;
    }

    public Float getDesignstage() {
        return designstage;
    }

    public void setDesignstage(Float designstage) {
        this.designstage = designstage;
    }

    public Float getCurrentstage() {
        return currentstage;
    }

    public void setCurrentstage(Float currentstage) {
        this.currentstage = currentstage;
    }

    public Float getToobjectstage() {
        return toobjectstage;
    }

    public void setToobjectstage(Float toobjectstage) {
        this.toobjectstage = toobjectstage;
    }

    public Float getTodesignstage() {
        return todesignstage;
    }

    public void setTodesignstage(Float todesignstage) {
        this.todesignstage = todesignstage;
    }

    public Float getRose2() {
        return rose2;
    }

    public void setRose2(Float rose2) {
        this.rose2 = rose2;
    }

    public Float getRose24() {
        return rose24;
    }

    public void setRose24(Float rose24) {
        this.rose24 = rose24;
    }

    public Float getTrend24() {
        return trend24;
    }

    public void setTrend24(Float trend24) {
        this.trend24 = trend24;
    }

    public static String[] getTitles() {
        return new String[]{"节制闸名称","目标水位(m)","设计水位(m)","当前水位(m)","距目标水位(m)","距设计水位(m)","2h涨幅(m)",
                            "24h涨幅(m)","24h闸前水位趋势"};
    }


    public AllLineSchedulingAnalysisBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeInt(this.jctid);
        dest.writeString(this.jctname);
        dest.writeValue(this.objectstage);
        dest.writeValue(this.designstage);
        dest.writeValue(this.currentstage);
        dest.writeValue(this.toobjectstage);
        dest.writeValue(this.todesignstage);
        dest.writeValue(this.rose2);
        dest.writeValue(this.rose24);
        dest.writeValue(this.trend24);
        dest.writeLong(this.queryDate != null ? this.queryDate.getTime() : -1);
    }

    protected AllLineSchedulingAnalysisBean(Parcel in) {
        this.jctid = in.readInt();
        this.jctname = in.readString();
        this.objectstage = (Float) in.readValue(Float.class.getClassLoader());
        this.designstage = (Float) in.readValue(Float.class.getClassLoader());
        this.currentstage = (Float) in.readValue(Float.class.getClassLoader());
        this.toobjectstage = (Float) in.readValue(Float.class.getClassLoader());
        this.todesignstage = (Float) in.readValue(Float.class.getClassLoader());
        this.rose2 = (Float) in.readValue(Float.class.getClassLoader());
        this.rose24 = (Float) in.readValue(Float.class.getClassLoader());
        this.trend24 = (Float) in.readValue(Float.class.getClassLoader());
        long tmpQueryDate = in.readLong();
        this.queryDate = tmpQueryDate == -1 ? null : new Date(tmpQueryDate);
    }

    public static final Creator<AllLineSchedulingAnalysisBean> CREATOR = new Creator<AllLineSchedulingAnalysisBean>() {
        @Override
        public AllLineSchedulingAnalysisBean createFromParcel(Parcel source) {
            return new AllLineSchedulingAnalysisBean(source);
        }

        @Override
        public AllLineSchedulingAnalysisBean[] newArray(int size) {
            return new AllLineSchedulingAnalysisBean[size];
        }
    };
}
