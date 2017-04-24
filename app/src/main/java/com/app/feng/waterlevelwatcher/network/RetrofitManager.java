package com.app.feng.waterlevelwatcher.network;

import com.app.feng.waterlevelwatcher.Config;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by feng on 2017/4/13.
 */

public class RetrofitManager {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    static GsonBuilder builder;

    static {
        builder = new GsonBuilder();
        builder.setDateFormat(Config.Constant.TIME_FORMAT);

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class,new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json,Type typeOfT,JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

    }

    private static Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.API.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    public static Retrofit getRetrofit() {
        return retrofit;
    }

}
