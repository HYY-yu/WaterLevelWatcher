package com.app.feng.waterlevelwatcher.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.feng.waterlevelwatcher.BuildConfig;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.network.BaseSubscriber;
import com.app.feng.waterlevelwatcher.network.RetrofitManager;
import com.app.feng.waterlevelwatcher.network.bean.ResponseBean;
import com.app.feng.waterlevelwatcher.network.interfaces.DataService;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.app.feng.waterlevelwatcher.utils.TimeRangeUtil;
import com.app.feng.waterlevelwatcher.utils.Utils;
import com.app.feng.waterlevelwatcher.utils.manager.LineChartManager;
import com.eleven.lib.library.ECSegmentedControl;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 进入后,判断数据库中是否有默认时间点的数据,没有就到服务器上去取,并保存或者更新到本地数据库.
 * 选择一个时间点,判断本地数据库中是否有该时间点的数据,有则显示,没有就去服务器上加载.
 * <p>
 * Created by feng on 2017/3/8.
 */

public class OverviewFragmentJZZ extends BaseLoadingFragment {

    private ISlidePanelEventControl panelControl;

    FloatingActionButton fab_change_time;

    LineChartView lineChart;
    ECSegmentedControl ec_change_data_type;

    RealmResults<SluiceBean> realmResults;
    LineChartManager lineChartManager;
    List<AxisValue> axisValues;
    List<PointValue> dataValueOpening;
    List<PointValue> dataValueFront;
    List<PointValue> dataValueBack;
    List<PointValue> data;

    Realm realm;

    String selectTime;

    public OverviewFragmentJZZ() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    public static OverviewFragmentJZZ newInstance(ISlidePanelEventControl panelEventControl) {
        OverviewFragmentJZZ overviewFragment = new OverviewFragmentJZZ();
        Bundle bundle = new Bundle();
        bundle.putSerializable("panelControl",panelEventControl);
        overviewFragment.setArguments(bundle);
        return overviewFragment;
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        setRetainInstance(true);
        this.panelControl = (ISlidePanelEventControl) getArguments().getSerializable("panelControl");

        View v = inflater.inflate(R.layout.fragment_ov_jzz,container,false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event) {
                return true;
            }
        });

        fab_change_time = (FloatingActionButton) v.findViewById(R.id.fab_chang_time);
        ec_change_data_type = (ECSegmentedControl) v.findViewById(R.id.ec_change_data_type);
        realm = Realm.getDefaultInstance();
        lineChart = (LineChartView) v.findViewById(R.id.lc_overview);

        lineChartManager = LineChartManager.getInstance(getContext().getApplicationContext());

        return v;
    }

    @Override
    public void onViewCreated(
            View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        initEvent();
        initData();
        initChart();
    }

    private void initChart() {
        mapData();

        lineChartManager.initChart(lineChart,panelControl,LineChartManager.MODE_FRAGMENT_JZZ);
        lineChartManager.initChartData(lineChart,axisValues,data,selectTime,
                                       LineChartManager.MODE_FRAGMENT_JZZ);
    }

    private void mapData() {
        List<String> XLabel = new ArrayList<>();
        List<Float> Y_Opening = new ArrayList<>();
        List<Float> Y_Front = new ArrayList<>();
        List<Float> Y_Back = new ArrayList<>();

        for (SluiceBean bean : realmResults) {
            XLabel.add(String.valueOf(bean.getSluiceID()));

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


    private void initData() {
        Calendar currentCalendar = Calendar.getInstance();
        String dEndTime = SharedPref.getInstance(getContext())
                .getString(Config.KEY.DEFAULT_END_TIME,Utils.format(new Date()));
        currentCalendar.setTime(Utils.parse(dEndTime));

        if (BuildConfig.DEBUG) {
            //取定值
            Date tempDate = Utils.parse(Config.Constant.GLOBAL_START_TIME);
            currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(tempDate);
        }

        selectTime = Utils.format(currentCalendar.getTime());
        realmResults = RealmUtil.loadDataByTime(realm,currentCalendar.getTime());
        if (Utils.isListEmpty(realmResults)) {
            loadFromNetworkSync(selectTime);
        }
    }

    private void initEvent() {
        final String dEndTime = SharedPref.getInstance(getContext())
                .getString(Config.KEY.DEFAULT_END_TIME,Utils.format(new Date()));
        fab_change_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeRangeUtil.showTimeSelector(getContext(),Config.Constant.GLOBAL_START_TIME,
                                               dEndTime,new TimeSelector.ResultHandler() {
                            @Override
                            public void handle(String s) {
                                selectTime = s;
                                handleTimeSelect();
                            }
                        });
            }

            private void handleTimeSelect() {
                // 加载其它时间的数据
                realmResults = RealmUtil.loadDataByTime(realm,Utils.parse(selectTime));

                if (Utils.isListEmpty(realmResults)) {
                    loadFromNetworkSync(selectTime);
                }else{
                    //重新初始化图表
                    initChart();
                }
            }
        });

        ec_change_data_type.setECSegmentedControlListener(
                new ECSegmentedControl.ECSegmentedControlListener() {
                    @Override
                    public void onSelectIndex(int index) {
                        switch (index) {
                            case 0:
                                lineChartManager.changeData(lineChart,dataValueOpening);
                                break;
                            case 1:
                                lineChartManager.changeData(lineChart,dataValueFront);
                                break;
                            case 2:
                                lineChartManager.changeData(lineChart,dataValueBack);
                                break;
                        }
                    }
                });
    }

    // 注意此方法是异步方法   不要在此方法下查询网络返回结果
    private void loadFromNetworkSync(String time) {
        RetrofitManager.getRetrofit()
                .create(DataService.class)
                .overviewAllStationByTime(time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<SluiceBean>(getContext().getApplicationContext()) {

                    @Override
                    public void onStartLoad() {
                        loadingData();
                    }

                    @Override
                    public void onEndLoad(boolean load) {
                        if(load){
                            loadComplete();
                        }else{
                            loadError();
                        }
                    }

                    @Override
                    public void onSuccess(
                            ResponseBean<SluiceBean> responseBean) {
                        convertNetToDB(responseBean.getDatas());
                        realmResults = RealmUtil.loadDataByTime(realm,Utils.parse(selectTime));
                        //重新初始化图表
                        initChart();
                    }

                });
    }

    @Override
    protected void reloadingData() {
        loadFromNetworkSync(selectTime);
    }

    private void convertNetToDB(List<SluiceBean> sluiceBeans) {
        if (Utils.isListEmpty(sluiceBeans)) {
            Toast.makeText(getContext(),"此时间无数据",Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        RealmUtil.saveToRealm(sluiceBeans);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}
