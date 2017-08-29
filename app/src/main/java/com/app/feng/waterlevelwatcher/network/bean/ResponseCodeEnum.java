package com.app.feng.waterlevelwatcher.network.bean;

/**
 * Created by feng on 2017/4/21.
 */
public enum ResponseCodeEnum {
    UNKNOW_ERROR(-1000,"未知错误"),
    SUCCESS(0,"成功"),
    REQUEST_PARAMETER_ERROR(-200,"请求的参数有误"),
    REQUEST_PARAMETER_NULL(-201,"缺少必要参数"),

    LOGIN_ERROR(-100,"用户名或密码错误")
    ;


    private int code;
    private String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
