package com.app.feng.waterlevelwatcher.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.inter.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.utils.AnimSet;
import com.app.feng.waterlevelwatcher.utils.FragmentUtil;
import com.app.feng.waterlevelwatcher.utils.LineChartManager;
import com.app.feng.waterlevelwatcher.utils.RealmUtils;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.eleven.lib.library.ECSegmentedControl;
import com.orhanobut.logger.Logger;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity implements ISlidePanelEventControl {

    BottomNavigationBar bottomNavigationBar;
    SlidingUpPanelLayout slidingUpPanelLayout;

    FragmentUtil fragmentUtil = new FragmentUtil(this);
    Realm realm;

    TextView tv_stationID;
    TextView tv_stationName;
    TextView tv_stationLatitude;
    TextView tv_stationLongitude;

    View fab_close_mini;
    private ScaleAnimation scaleAnimation_show;
    private ScaleAnimation scaleAnimation_hidden;

    LineChartView lineChart;
    ECSegmentedControl ec_change_data_type;

    LineChartManager lineChartManager;

    List<AxisValue> axisValues;
    List<PointValue> dataValueOpening;
    List<PointValue> dataValueFront;
    List<PointValue> dataValueBack;
    List<PointValue> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initFragment();

        initEvent();

    }

    private void initEvent() {
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        fragmentUtil.setMapFragment();
                        break;
                    case 1:
                        fragmentUtil.setOverviewFragment();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:

                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        fab_close_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePanel();
            }
        });

        findViewById(R.id.fab_close_panel_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePanel();
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel,float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(
                    View panel,SlidingUpPanelLayout.PanelState previousState,
                    SlidingUpPanelLayout.PanelState newState) {
                switch (newState) {
                    case COLLAPSED:
                        //显示close按钮
                        if (!fab_close_mini.isShown()) {
                            fab_close_mini.startAnimation(scaleAnimation_show);
                            fab_close_mini.setVisibility(View.VISIBLE);
                        }

                        break;
                    default:
                        if (fab_close_mini.isShown()) {
                            fab_close_mini.startAnimation(scaleAnimation_hidden);
                            fab_close_mini.setVisibility(View.GONE);
                        }
                }
            }
        });
    }

    private void initFragment() {
        fragmentUtil.addAllFragment();
        fragmentUtil.setMapFragment();
    }

    private void initView() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        VectorDrawableCompat vectorDrawableCompat_map = VectorDrawableCompat.create(getResources(),
                                                                                    R.drawable.ic_map_black_24dp,
                                                                                    null);
        VectorDrawableCompat vectorDrawableCompat_Chart = VectorDrawableCompat.create(
                getResources(),R.drawable.ic_chart_black_24dp,null);
        VectorDrawableCompat vectorDrawableCompat_TJ = VectorDrawableCompat.create(getResources(),
                                                                                   R.drawable.ic_tongji_black_24dp,
                                                                                   null);
        VectorDrawableCompat vectorDrawableCompat_Setting = VectorDrawableCompat.create(
                getResources(),R.drawable.ic_settings_black_24dp,null);

        bottomNavigationBar.addItem(new BottomNavigationItem(vectorDrawableCompat_map,"地图"))
                .addItem(new BottomNavigationItem(vectorDrawableCompat_Chart,"概览"))
                .addItem(new BottomNavigationItem(vectorDrawableCompat_TJ,"统计"))
                .addItem(new BottomNavigationItem(vectorDrawableCompat_Setting,"管理"))
                .initialise();


        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.spl_main_content);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);


        tv_stationID = (TextView) findViewById(R.id.station_ID);
        tv_stationLatitude = (TextView) findViewById(R.id.station_latitude);
        tv_stationLongitude = (TextView) findViewById(R.id.station_longitude);
        tv_stationName = (TextView) findViewById(R.id.station_name);

        LinearLayout slideContent = (LinearLayout) findViewById(R.id.slide_panel_content);
        slideContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event) {
                return true;
            }
        });

        fab_close_mini = findViewById(R.id.fab_close_panel_mini);
        realm = Realm.getDefaultInstance();

        scaleAnimation_show = AnimSet.getScaleAnimationFABSHOW();
        scaleAnimation_hidden = AnimSet.getScaleAnimationFABHIDDEN();

        lineChartManager = LineChartManager.getInstanse(this.getApplicationContext());

        lineChart = (LineChartView) findViewById(R.id.lc_station);
        ec_change_data_type = (ECSegmentedControl) findViewById(R.id.ec_station_change_data);

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
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    @Override
    public void openPanel(int sluiceID) {
        String stringID = getResources().getString(R.string.station_Id);
        String stringLa = getResources().getString(R.string.station_latitude);
        String stringLo = getResources().getString(R.string.station_longitude);

        //根据sluiceID 向Realm查询其数据
        MonitoringStationBean theBean = RealmUtils.loadStationDataById(realm,sluiceID);
        RealmResults<SluiceBean> stationDatas = RealmUtils.loadDataById(realm,sluiceID);

        tv_stationID.setText(String.format(stringID,theBean.getSluiceID()));
        tv_stationName.setText(theBean.getName());
        tv_stationLongitude.setText(String.format(stringLo,theBean.getLongitude()));
        tv_stationLatitude.setText(String.format(stringLa,theBean.getLatitude()));

        //初始化图表
        //X 单位 时间
        mapData(stationDatas);

        lineChartManager.initChart(lineChart,null,LineChartManager.MODE_PANEL);
        lineChartManager.initChartData(lineChart,axisValues,data,"  ",LineChartManager.MODE_PANEL);
        ec_change_data_type.setSelectedIndex(0);

        //打开Panel
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //处理正常的搜索查询案例
            String query = intent.getStringExtra(SearchManager.QUERY);
            Logger.d("收到 query + " + query);

            //忽略用户正常的点击搜索按钮,因其没有必要(当输入一定字符时,列表已经出现搜索结果)
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            //处理建议点击(因为建议都使用ACTION_VIEW)
            String data = intent.getDataString();
            Logger.d("收到 data + " + data);

            try {
                int id = Integer.parseInt(data);

                //通知高德地图转移到相应的监测站
                if(fragmentUtil.getCurrentFragment() instanceof MapFragment){
                    MapFragment mapFragment = (MapFragment) fragmentUtil.getCurrentFragment();
                    mapFragment.moveMapToStation(id);
                    openPanel(id);

                }
            }catch (NumberFormatException e){
                Logger.d("此 Intent 不来自搜索" + intent);
            }
        }
    }

    @Override
    public void closePanel() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }
}
