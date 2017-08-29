package com.app.feng.waterlevelwatcher.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.Hour24SluiceBean;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.network.BaseSubscriber;
import com.app.feng.waterlevelwatcher.network.RetrofitManager;
import com.app.feng.waterlevelwatcher.network.bean.ResponseBean;
import com.app.feng.waterlevelwatcher.network.interfaces.DataService;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.Utils;
import com.app.feng.waterlevelwatcher.utils.manager.LineChartManager;
import com.app.feng.waterlevelwatcher.utils.manager.LoadingDialogManager;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import lecho.lib.hellocharts.view.LineChartView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Hour24JzzActivity extends AppCompatActivity {

    TextView tv_station_name;

    Realm realm;

    LineChartView lineChartView;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour24_jzz);
        realm = Realm.getDefaultInstance();

        tv_station_name = (TextView) findViewById(R.id.tv_station_name);
        lineChartView = (LineChartView) findViewById(R.id.lineCharView);

        LineChartManager.getInstance(getBaseContext())
                .initChart(lineChartView,null,LineChartManager.MODE_ACTIVITY);

        int id = getIntent().getIntExtra(Config.KEY.HOUR24ID,1);
        String time = getIntent().getStringExtra(Config.KEY.HOUR24TIME);

        MonitoringStationBean stationBean = RealmUtil.loadStationDataById(realm,id);

        tv_station_name.setText(stationBean.getName());

        //读网查询
        RetrofitManager.getRetrofit()
                .create(DataService.class)
                .hour24SluiceBeanList(time,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Hour24SluiceBean>(getBaseContext()) {
                    @Override
                    public void onStartLoad() {
                        dialog = LoadingDialogManager.openDialog(Hour24JzzActivity.this);
                    }

                    @Override
                    public void onEndLoad(boolean loadSuccess) {
                        LoadingDialogManager.closeDialog(dialog);
                        if (!loadSuccess) {
                            finish();
                        }
                    }

                    @Override
                    public void onSuccess(
                            ResponseBean<Hour24SluiceBean> responseBean) {
                        //共24条数据

                        List<Hour24SluiceBean> hour24SluiceBeans = responseBean.getDatas();
                        //如果查不到 ,这24条 数据均为空,服务器将返回空列表
                        if (Utils.isListEmpty(hour24SluiceBeans)) {
                            Toast.makeText(getApplicationContext(),"本时间点节制闸无数据",Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                            return;
                        }

                        List<Float> y_data = new ArrayList<>();
                        List<String> x_lable = new ArrayList<>();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd 日 HH 时");

                        for (Hour24SluiceBean b : hour24SluiceBeans) {
                            y_data.add(b.getFrontwl());
                            x_lable.add(simpleDateFormat.format(b.getJctime()));
                            Logger.i("看看每条数据 : " + "jcttime " + simpleDateFormat.format(
                                    b.getJctime()) + " 的闸前水位是：" + b.getFrontwl());
                        }

                        LineChartManager lineChartManager = LineChartManager.getInstance
                                (getBaseContext());
                        lineChartManager.initChartData
                                (lineChartView,lineChartManager.mapXAxis(x_lable),
                                 lineChartManager.mapDataValue(y_data)," ",LineChartManager
                                         .MODE_ACTIVITY);
                    }

                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}
