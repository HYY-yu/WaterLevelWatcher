package com.app.feng.waterlevelwatcher.network;

import android.content.Context;
import android.widget.Toast;

import com.app.feng.waterlevelwatcher.network.bean.ResponseBean;
import com.app.feng.waterlevelwatcher.network.bean.ResponseCodeEnum;
import com.app.feng.waterlevelwatcher.utils.Utils;
import com.orhanobut.logger.Logger;

import rx.Subscriber;

/**
 * Created by feng on 2017/4/21.
 */

public abstract class BaseSubscriber<T> extends Subscriber<ResponseBean<T>> {

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!Utils.isConnected(context)) {
            // 当前网络不可用
            Toast.makeText(context,"当前网络不可用，请检查链接",Toast.LENGTH_SHORT)
                    .show();
            onCompleted();
            onEndLoad(false);
        }

        onStartLoad();
    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e," -- 网络错误 :" + e.getMessage());
        Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT)
                .show();

        onEndLoad(false);
    }

    @Override
    public void onNext(ResponseBean<T> responseBean) {
        if (responseBean.getCode() == ResponseCodeEnum.SUCCESS.getCode()) {
            onSuccess(responseBean);
        } else {
            Logger.i(" -- 服务器报错 :" + responseBean.getMessage());
            Toast.makeText(context,responseBean.getMessage(),Toast.LENGTH_SHORT)
                    .show();
            onFail(responseBean);
        }
        onEndLoad(true);
    }

    public abstract void onStartLoad();

    public abstract void onSuccess(ResponseBean<T> responseBean);

    public void onFail(ResponseBean<T> responseBean) {

    }

    public abstract void onEndLoad(boolean loadSuccess);

    @Override
    public void onCompleted() {

    }
}
