package com.app.feng.waterlevelwatcher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;

import com.app.feng.waterlevelwatcher.BuildConfig;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.inter.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.log.XLog;
import com.app.feng.waterlevelwatcher.utils.AnimSet;
import com.app.feng.waterlevelwatcher.utils.LineChartManager;
import com.app.feng.waterlevelwatcher.utils.RealmUtils;
import com.eleven.lib.library.ECSegmentedControl;

import org.feezu.liuli.timeselector.TimeSelector;
import org.feezu.liuli.timeselector.Utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by feng on 2017/3/8.
 */

public class OverviewFragment extends Fragment {

    private ISlidePanelEventControl panelControl;

    FloatingActionButton fab_change_time;

    ScaleAnimation scaleAnimation_show;
    ScaleAnimation scaleAnimation_hidden;

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

    String currentTime = DateUtil.format(new Date(),Config.Constant.TIME_FORMAT);
    String selectTime;

    public OverviewFragment() {
        XLog.d("OverviewFragment : " + "OverviewFragment");
        scaleAnimation_show = AnimSet.getScaleAnimationFABSHOW();
        scaleAnimation_hidden = AnimSet.getScaleAnimationFABHIDDEN();
    }

    public static OverviewFragment getInstance() {
        OverviewFragment overviewFragment = new OverviewFragment();
        return overviewFragment;
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview,container,false);
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

        lineChartManager = LineChartManager.getInstanse(getContext().getApplicationContext());

        XLog.d("OverviewFragment : " + "onCreateView");

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

        lineChartManager.initChart(lineChart,panelControl,LineChartManager.MODE_FRAGMENT);
        lineChartManager.initChartData(lineChart,axisValues,data,selectTime,LineChartManager.MODE_FRAGMENT);
    }

    private void mapData() {
        List<String> XLabel = new ArrayList<>();
        List<Float> Y_Opening = new ArrayList<>();
        List<Float> Y_Front = new ArrayList<>();
        List<Float> Y_Back = new ArrayList<>();

        for (SluiceBean bean : realmResults) {
            XLabel.add(bean.getSluiceID() + "");
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

    private void initData() {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(DateUtil.parse(currentTime,Config.Constant.TIME_FORMAT));
        int hour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour % 2 != 0) {
            //若为奇数
            --hour;
        }
        currentCalendar.set(Calendar.HOUR_OF_DAY,hour);
        XLog.d(currentCalendar.toString());

        if (BuildConfig.DEBUG) {
            //取定值
            Date tempDate = DateUtil.parse("2015/12/24 00:00",Config.Constant.TIME_FORMAT);
            currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(tempDate);
        }

        selectTime = DateUtil.format(currentCalendar.getTime(),Config.Constant.TIME_FORMAT);
        realmResults = RealmUtils.loadDataByTime(realm,selectTime);
        XLog.d(realmResults.toString());

    }

    private void initEvent() {
        fab_change_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开TimePicker
                TimeSelector timeSelector = new TimeSelector(getContext(),
                                                             new TimeSelector.ResultHandler() {
                                                                 @Override
                                                                 public void handle(String time) {
                                                                     //select new Time
                                                                     XLog.d("select:" + time);

                                                                     selectTime = time;
                                                                     // 加载其它时间的数据
                                                                     realmResults = RealmUtils.loadDataByTime(
                                                                             realm,time);
                                                                     //重新初始化图表
                                                                     initChart();

                                                                 }
                                                             },"2015/12/24 00:00",currentTime);

                timeSelector.disScrollUnit(TimeSelector.SCROLLTYPE.MINUTE);
                timeSelector.show();
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (fab_change_time != null) {
            if (!hidden) {
                fab_change_time.startAnimation(scaleAnimation_show);
            } else {
                fab_change_time.startAnimation(scaleAnimation_hidden);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        XLog.d("OverviewFragment : " + "onResume");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ISlidePanelEventControl) {
            panelControl = (ISlidePanelEventControl) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement ISlidePanelEventControl");
        }
        XLog.d("OverviewFragment : " + "onAttach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        panelControl = null;
        XLog.d("OverviewFragment : " + "onDetach");
    }


    @Override
    public void onPause() {
        super.onPause();
        XLog.d("OverviewFragment : " + "onPause");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        XLog.d("OverviewFragment : " + "onSaveInstanceState");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
        XLog.d("OverviewFragment : " + "onDestroy");
    }
}
