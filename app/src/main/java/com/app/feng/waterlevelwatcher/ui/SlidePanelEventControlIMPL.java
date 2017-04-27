package com.app.feng.waterlevelwatcher.ui;

import android.app.Dialog;
import android.widget.Toast;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.network.BaseSubscriber;
import com.app.feng.waterlevelwatcher.network.RetrofitManager;
import com.app.feng.waterlevelwatcher.network.bean.ResponseBean;
import com.app.feng.waterlevelwatcher.network.interfaces.DataService;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.Utils;
import com.app.feng.waterlevelwatcher.utils.manager.LineChartManager;
import com.app.feng.waterlevelwatcher.utils.manager.LoadingDialogManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by feng on 2017/3/23.
 */

public class SlidePanelEventControlIMPL implements ISlidePanelEventControl {

    //    String stringLo;
    //    String stringLa;
    //    private ChangePositionRealmListener changePositionRealmListener;
    private String stringIDName;

    private List<AxisValue> axisValues;
    private List<PointValue> dataValueOpening;
    private List<PointValue> dataValueFront;
    private List<PointValue> dataValueBack;
    private List<PointValue> data;

    private Realm realm;

    private LineChartManager lineChartManager;

    private MainActivity mainActivity;

    private int nowSluiceID = -1;

    MonitoringStationBean theBean;
    RealmResults<SluiceBean> stationDatas;

    public SlidePanelEventControlIMPL(MainActivity mainActivity,Realm realm) {
        this.realm = realm;
        this.mainActivity = mainActivity;

        stringIDName = mainActivity.getResources()
                .getString(R.string.station_Id_name);
        //        changePositionRealmListener = new ChangePositionRealmListener();
        //        stringLa = mainActivity.getResources()
        //                .getString(R.string.station_latitude);
        //        stringLo = mainActivity.getResources()
        //                .getString(R.string.station_longitude);

        lineChartManager = LineChartManager.getInstance(mainActivity.getApplicationContext());
    }

    public int getNowSluiceID() {
        return nowSluiceID;
    }

    @Override
    public void openPanel(int sluiceID) {
        //与当前的SluiceId相同，不必查询  (问题 : 这段代码用于重复点击同一个节制闸,不会再次查询数据 .然而会带来无法查询选定时间段数据的问题)
        if (nowSluiceID == sluiceID) {
            showPanel();
            return;
        }
        nowSluiceID = sluiceID;

        //所以我们将 openPanel() 拆分
        queryNewTimeRange();

    }

    public void queryNewTimeRange() {
        queryDatabase(nowSluiceID);
        initPanelView();

        if (shouldLoadFromNetwork()) {
            loadFromNetworkSync(nowSluiceID);
            return;
        }

        showPanel();
    }

    private void showPanel() {
        mainActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void closePanel() {
        mainActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    private void initPanelView() {
        mainActivity.initTextViewIDName(stringIDName,theBean);
        //        位置相关代码
        //        theBean.addChangeListener(changePositionRealmListener);
        //        mainActivity.initStationPosition(theBean,stringLo,stringLa);

        //初始化图表
        //X 单位 时间
        mapData(stationDatas);

        lineChartManager.initChart(mainActivity.lineChart,null,LineChartManager.MODE_PANEL);
        lineChartManager.initChartData(mainActivity.lineChart,axisValues,data,"  ",
                                       LineChartManager.MODE_PANEL);
        mainActivity.ec_change_data_type.setSelectedIndex(0);
        try {
            Field field = mainActivity.ec_change_data_type.getClass()
                    .getDeclaredField("mTouchIndex");
            field.setAccessible(true);
            field.set(mainActivity.ec_change_data_type,-1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadFromNetworkSync(final int sluiceID) {
        String timeStartString = (String) mainActivity.tv_starttime.getTag();
        String timeEndString = (String) mainActivity.tv_endtime.getTag();

        //读网查询
        RetrofitManager.getRetrofit()
                .create(DataService.class)
                .queryOneStationByTimeRange(String.valueOf(sluiceID),timeStartString,timeEndString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<SluiceBean>(mainActivity.getApplicationContext()) {
                    Dialog dialog;

                    @Override
                    public void onStartLoad() {
                        //打开加载框
                        dialog = LoadingDialogManager.openDialog(mainActivity);
                    }

                    @Override
                    public void onEndLoad(boolean load) {
                        LoadingDialogManager.closeDialog(dialog);
                    }

                    @Override
                    public void onSuccess(
                            ResponseBean<SluiceBean> responseBean) {
                        showPanel();
                        List<SluiceBean> sluiceBeanList = responseBean.getDatas();
                        if (Utils.isListEmpty(sluiceBeanList)) {
                            Toast.makeText(mainActivity,"当前时间段没有数据",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //保存数据库
                        RealmUtil.saveToRealm(sluiceBeanList);

                        //重新查询
                        queryDatabase(sluiceID);
                        initPanelView();
                        showPanel();
                    }
                });
    }

    private boolean shouldLoadFromNetwork() {
        return Utils.isListEmpty(stationDatas);
    }

    private void queryDatabase(int sluiceID) {
        //查询起始、结束字符串
        String timeStartString = (String) mainActivity.tv_starttime.getTag();
        String timeEndString = (String) mainActivity.tv_endtime.getTag();

        //根据sluiceID 向Realm查询其数据
        theBean = RealmUtil.loadStationDataById(realm,sluiceID);
        stationDatas = RealmUtil.loadDataByIdAndTimeRange(realm,sluiceID,timeStartString,
                                                          timeEndString);
    }

    private void mapData(RealmResults<SluiceBean> stationDatas) {
        List<String> XLabel = new ArrayList<>();
        List<Float> Y_Opening = new ArrayList<>();
        List<Float> Y_Front = new ArrayList<>();
        List<Float> Y_Back = new ArrayList<>();

        for (SluiceBean bean : stationDatas) {
            String formatTime = Utils.format(bean.getTime());
            XLabel.add(formatTime.substring(5,formatTime.length()));

            Y_Opening.add(bean.getSluiceOpening1());
            Y_Front.add(bean.getWaterLevel_front());
            Y_Back.add(bean.getWaterLevel_back());
        }

        axisValues = lineChartManager.mapXAxis(XLabel);
        dataValueOpening = lineChartManager.mapDataValue(Y_Opening);
        dataValueFront = lineChartManager.mapDataValue(Y_Front);
        dataValueBack = lineChartManager.mapDataValue(Y_Back);
        data = new ArrayList<>();

        //data 在changeData的时候会被冲掉, 深拷贝一份专用于Chart
        for (PointValue old : dataValueOpening) {
            data.add(new PointValue(old.getX(),old.getY()));
        }
    }

    @Override
    public boolean isPanelOpen() {
        SlidingUpPanelLayout.PanelState state = mainActivity.slidingUpPanelLayout.getPanelState();
        return state != SlidingUpPanelLayout.PanelState.HIDDEN;
    }

    void changeDataOpening() {
        lineChartManager.changeData(mainActivity.lineChart,dataValueOpening);
    }

    void changeDataFront() {
        lineChartManager.changeData(mainActivity.lineChart,dataValueFront);

    }

    void changeDataBlack() {
        lineChartManager.changeData(mainActivity.lineChart,dataValueBack);
    }



    //    private class ChangePositionRealmListener implements RealmChangeListener<MonitoringStationBean> {
    //        @Override
    //        public void onChange(MonitoringStationBean element) {
    //            // 当数据有更新,Realm会自动提示
    //            mainActivity.initStationPosition(element,stringLo,stringLa);
    //
    //            //将此处的位置更新映射到Map上
    //            mainActivity.fragmentUtil.mapFragment.markerManager.updateMarker(element);
    //        }
    //    }

}
