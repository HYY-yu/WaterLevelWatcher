package com.app.feng.waterlevelwatcher.bean;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by feng on 2017/4/26.
 */
public class AllLineFlowSpeedBean extends RealmObject{

    //jctid   节制闸编号
    //jctname varchar(100),		--节制闸名称
    //location varchar(100),		--安装位置
    //timepoint1 float,			--时间点1
    //timepoint2 float,			--时间点2
    //timepoint3 float,			--时间点3
    //timepoint4 float,			--时间点4
    //timepoint5 float,			--时间点5
    //timepoint6 float,			--时间点6
    //timepoint7 float,			--时间点7
    //timepoint8 float,			--时间点8
    //timepoint9 float,			--时间点9
    //timepoint10 float,			--时间点10
    //timepoint11 float,			--时间点11
    //timepoint12 float			--时间点12

    private int jctid;
    private String jctname;
    private String location;
    private Float timepoint1;
    private Float timepoint2;
    private Float timepoint3;
    private Float timepoint4;
    private Float timepoint5;
    private Float timepoint6;
    private Float timepoint7;
    private Float timepoint8;
    private Float timepoint9;
    private Float timepoint10;
    private Float timepoint11;
    private Float timepoint12;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getTimepoint1() {
        return timepoint1;
    }

    public void setTimepoint1(Float timepoint1) {
        this.timepoint1 = timepoint1;
    }

    public Float getTimepoint2() {
        return timepoint2;
    }

    public void setTimepoint2(Float timepoint2) {
        this.timepoint2 = timepoint2;
    }

    public Float getTimepoint3() {
        return timepoint3;
    }

    public void setTimepoint3(Float timepoint3) {
        this.timepoint3 = timepoint3;
    }

    public Float getTimepoint4() {
        return timepoint4;
    }

    public void setTimepoint4(Float timepoint4) {
        this.timepoint4 = timepoint4;
    }

    public Float getTimepoint5() {
        return timepoint5;
    }

    public void setTimepoint5(Float timepoint5) {
        this.timepoint5 = timepoint5;
    }

    public Float getTimepoint6() {
        return timepoint6;
    }

    public void setTimepoint6(Float timepoint6) {
        this.timepoint6 = timepoint6;
    }

    public Float getTimepoint7() {
        return timepoint7;
    }

    public void setTimepoint7(Float timepoint7) {
        this.timepoint7 = timepoint7;
    }

    public Float getTimepoint8() {
        return timepoint8;
    }

    public void setTimepoint8(Float timepoint8) {
        this.timepoint8 = timepoint8;
    }

    public Float getTimepoint9() {
        return timepoint9;
    }

    public void setTimepoint9(Float timepoint9) {
        this.timepoint9 = timepoint9;
    }

    public Float getTimepoint10() {
        return timepoint10;
    }

    public void setTimepoint10(Float timepoint10) {
        this.timepoint10 = timepoint10;
    }

    public Float getTimepoint11() {
        return timepoint11;
    }

    public void setTimepoint11(Float timepoint11) {
        this.timepoint11 = timepoint11;
    }

    public Float getTimepoint12() {
        return timepoint12;
    }

    public void setTimepoint12(Float timepoint12) {
        this.timepoint12 = timepoint12;
    }
}
