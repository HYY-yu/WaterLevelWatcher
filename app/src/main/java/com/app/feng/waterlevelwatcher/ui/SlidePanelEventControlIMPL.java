package com.app.feng.waterlevelwatcher.ui;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.manager.LineChartManager;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by feng on 2017/3/23.
 */

public class SlidePanelEventControlIMPL implements ISlidePanelEventControl {

    private ChangePositionRealmListener changePositionRealmListener;
    String stringID;
    String stringLa;
    String stringLo;

    List<AxisValue> axisValues;
    List<PointValue> dataValueOpening;
    List<PointValue> dataValueFront;
    List<PointValue> dataValueBack;
    List<PointValue> data;

    Realm realm;

    LineChartManager lineChartManager;

    MainActivity mainActivity;

    public SlidePanelEventControlIMPL(MainActivity mainActivity,Realm realm) {
        this.realm = realm;
        this.mainActivity = mainActivity;

        changePositionRealmListener = new ChangePositionRealmListener();
        stringID = mainActivity.getResources()
                .getString(R.string.station_Id);
        stringLa = mainActivity.getResources()
                .getString(R.string.station_latitude);
        stringLo = mainActivity.getResources()
                .getString(R.string.station_longitude);

        lineChartManager = LineChartManager.getInstanse(mainActivity.getApplicationContext());
    }

    @Override
    public void openPanel(int sluiceID) {
        //根据sluiceID 向Realm查询其数据
        MonitoringStationBean theBean = RealmUtil.loadStationDataById(realm,sluiceID);
        RealmResults<SluiceBean> stationDatas = RealmUtil.loadDataById(realm,sluiceID);

        theBean.addChangeListener(changePositionRealmListener);

        mainActivity.initTextViewIDName(stringID,theBean);
        mainActivity.initStationPosition(theBean,stringLo,stringLa);

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

        //打开Panel
        mainActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void closePanel() {
        mainActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    private void mapData(RealmResults<SluiceBean> realmResults) {
        List<String> XLabel = new ArrayList<>();
        List<Float> Y_Opening = new ArrayList<>();
        List<Float> Y_Front = new ArrayList<>();
        List<Float> Y_Back = new ArrayList<>();

        for (SluiceBean bean : realmResults) {
            XLabel.add(bean.getFormatTime()
                               .substring(5,bean.getFormatTime()
                                       .length()));

            Y_Opening.add(bean.getSluiceOpening());
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

    public void changeDataOpening() {
        lineChartManager.changeData(mainActivity.lineChart,dataValueOpening);
    }

    public void changeDataFront() {
        lineChartManager.changeData(mainActivity.lineChart,dataValueFront);

    }

    public void changeDataBlack() {
        lineChartManager.changeData(mainActivity.lineChart,dataValueBack);
    }

    private class ChangePositionRealmListener implements RealmChangeListener<MonitoringStationBean> {
        @Override
        public void onChange(MonitoringStationBean element) {
            // 当数据有更新,Realm会自动提示
            mainActivity.initStationPosition(element,stringLo,stringLa);

            //将此处的位置更新映射到Map上
            mainActivity.fragmentUtil.mapFragment.markerManager.updateMarker(element);
            mainActivity.fragmentUtil.mapFragment.aMap.postInvalidate();
        }
    }

}
