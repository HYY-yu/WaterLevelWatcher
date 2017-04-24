package com.app.feng.waterlevelwatcher.network.interfaces;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
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
}
