package com.app.feng.waterlevelwatcher.network.interfaces;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.bean.*;
import com.app.feng.waterlevelwatcher.network.bean.ResponseBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by feng on 2017/4/18.
 */

public interface DataService {

    @FormUrlEncoded
    @POST(Config.API.overviewAllStationUrl)
    Observable<ResponseBean<SluiceBean>> overviewAllStationByTime(@Field("time") String time);

    @FormUrlEncoded
    @POST(Config.API.queryOneStationByTimeRange)
    Observable<ResponseBean<SluiceBean>> queryOneStationByTimeRange(
            @Field("sluiceID") String sluiceID,@Field("timeStartString") String timeStartString,
            @Field("timeEndString") String timeEndString);

    @FormUrlEncoded
    @POST(Config.API.allScheduleAnalysis)
    Observable<ResponseBean<AllLineSchedulingAnalysisBean>> allScheduleAnalysis(
            @Field("datetime") String time,@Field("currentPage") int currentPage,
            @Field("countPage") int countPage);

    @FormUrlEncoded
    @POST(Config.API.allLineFLowSpeed)
    Observable<ResponseBean<AllLineFlowSpeedBean>> allLineFLowSpeed(
            @Field("datetime") String time,@Field("currentPage") int currentPage,
            @Field("countPage") int countPage);

    @FormUrlEncoded
    @POST(Config.API.allLineWaterDepth)
    Observable<ResponseBean<AllLineWaterDepthBean>> allLineWaterDepth(
            @Field("datetime") String time,@Field("currentPage") int currentPage,
            @Field("countPage") int countPage);

    @FormUrlEncoded
    @POST(Config.API.jzzFrontWLTrend)
    Observable<ResponseBean<Hour24SluiceBean>> hour24SluiceBeanList(
            @Field("time") String time,@Field("jctid") int id);

    @FormUrlEncoded
    @POST(Config.API.fsStatistics)
    Observable<ResponseBean<FS_StatisticsBean>> fsStatistics(
            @Field("time") String time);

    @FormUrlEncoded
    @POST(Config.API.zddmStatistics)
    Observable<ResponseBean<ZDDM_StatisticsBean>> zddmStatistics(
            @Field("time") String time);


}
