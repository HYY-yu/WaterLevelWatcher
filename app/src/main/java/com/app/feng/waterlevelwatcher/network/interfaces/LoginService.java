package com.app.feng.waterlevelwatcher.network.interfaces;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.bean.UserBean;
import com.app.feng.waterlevelwatcher.network.bean.ResponseBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by feng on 2017/4/13.
 */

public interface LoginService {

    @FormUrlEncoded
    @POST(Config.API.loginUrl)
    Observable<ResponseBean<UserBean>> login(
            @Field("loginName") String username,@Field("password") String password);
}
