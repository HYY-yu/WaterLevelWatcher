package com.app.feng.waterlevelwatcher.network.bean;

import java.util.List;

/**
 * Created by feng on 2017/4/18.
 */

public class OverviewAllStationBean {
    public int total;

    public List<StationBean> rows;
    public static class StationBean {
        Integer autobackwl;
        Integer autofrontwl;
        Integer jctid;
        String jctname;

        //文字
        String holeleftkd;
        //图表
        Integer hole1leftkd;

        public Integer getAutobackwl() {
            return autobackwl;
        }

        public void setAutobackwl(Integer autobackwl) {
            this.autobackwl = autobackwl;
        }

        public Integer getAutofrontwl() {
            return autofrontwl;
        }

        public void setAutofrontwl(Integer autofrontwl) {
            this.autofrontwl = autofrontwl;
        }

        public Integer getJctid() {
            return jctid;
        }

        public void setJctid(Integer jctid) {
            this.jctid = jctid;
        }

        public String getJctname() {
            return jctname;
        }

        public void setJctname(String jctname) {
            this.jctname = jctname;
        }

        public String getHoleleftkd() {
            return holeleftkd;
        }

        public void setHoleleftkd(String holeleftkd) {
            this.holeleftkd = holeleftkd;
        }

        public Integer getHole1leftkd() {
            return hole1leftkd;
        }

        public void setHole1leftkd(Integer hole1leftkd) {
            this.hole1leftkd = hole1leftkd;
        }
    }
}
