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

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.ui.fragment.MapFragment;
import com.app.feng.waterlevelwatcher.utils.AnimSetUtil;
import com.app.feng.waterlevelwatcher.utils.DialogUtil;
import com.app.feng.waterlevelwatcher.utils.FragmentUtil;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.eleven.lib.library.ECSegmentedControl;
import com.orhanobut.logger.Logger;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import io.realm.Realm;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationBar bottomNavigationBar;
    SlidingUpPanelLayout slidingUpPanelLayout;

    FragmentUtil fragmentUtil = new FragmentUtil(this);
    Realm realm;

    TextView tv_stationID;
    TextView tv_stationName;
    TextView tv_stationLatitude;
    TextView tv_stationLongitude;
    TextView tv_starttime;
    TextView tv_endtime;

    View ll_change_position;
    View ll_change_time_area;

    View fab_close_mini;

    private ScaleAnimation scaleAnimation_show;
    private ScaleAnimation scaleAnimation_hidden;

    LineChartView lineChart;
    ECSegmentedControl ec_change_data_type;

    public SlidePanelEventControlIMPL slidePanelEventControlIMPL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initFragment();

        initEvent();

        initTime();
    }

    private void initTime() {
        String startTime = SharedPref.getInstance(this)
                .getString(Config.KEY.DEFAULT_START_TIME,"");

        String endTime = SharedPref.getInstance(this)
                .getString(Config.KEY.DEFAULT_END_TIME,"");

        String startTimeString = getString(R.string.start_time_string);
        String endTimeString = getString(R.string.end_time_string);

        tv_starttime.setText(String.format(startTimeString,startTime));
        tv_endtime.setText(String.format(endTimeString,endTime));
    }

    private void showMask() {
        if (!SharedPref.getInstance(this)
                .getBoolean(Config.KEY.SHOW_MASK,false)) {

            new MaterialIntroView.Builder(this).enableDotAnimation(true)
                    .enableFadeAnimation(true)
                    .enableIcon(false)
                    .performClick(true)
                    .dismissOnTouch(true)
                    .setTarget(ll_change_position)
                    .setInfoText("点击此处可修改当前监测站的地理位置")
                    .setIdempotent(true)
                    .setUsageId("mask1")
                    .setListener(new MaterialIntroListener() {
                        @Override
                        public void onUserClicked(String s) {
                            new MaterialIntroView.Builder(MainActivity.this).enableDotAnimation(
                                    true)
                                    .enableFadeAnimation(true)
                                    .enableIcon(false)
                                    .dismissOnTouch(true)
                                    .setShape(ShapeType.RECTANGLE)
                                    .setFocusType(Focus.NORMAL)
                                    .performClick(false)
                                    .setTarget(ll_change_time_area)
                                    .setInfoText("点击此处可浏览其它时间区间的数据")
                                    .setUsageId("mask2")
                                    .show();
                        }
                    })
                    .show();


            SharedPref.getInstance(this)
                    .putBoolean(Config.KEY.SHOW_MASK,true);
        }

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
                        fragmentUtil.setSettingFragment();
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
                slidePanelEventControlIMPL.closePanel();
            }
        });

        findViewById(R.id.fab_close_panel_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidePanelEventControlIMPL.closePanel();
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
                if (fragmentUtil.getCurrentFragment() instanceof MapFragment) {
                    MapFragment mapFragment = (MapFragment) fragmentUtil.getCurrentFragment();
                    mapFragment.clearSearchViewFocus();
                }
                switch (newState) {
                    case COLLAPSED:
                        //显示close按钮
                        if (!fab_close_mini.isShown()) {
                            fab_close_mini.startAnimation(scaleAnimation_show);
                            fab_close_mini.setVisibility(View.VISIBLE);
                        }

                        break;
                    case EXPANDED:
                        showMask();

                        //取消break
                    default:
                        if (fab_close_mini.isShown()) {
                            fab_close_mini.startAnimation(scaleAnimation_hidden);
                            fab_close_mini.setVisibility(View.GONE);
                        }
                }
            }
        });

        ec_change_data_type.setECSegmentedControlListener(
                new ECSegmentedControl.ECSegmentedControlListener() {
                    @Override
                    public void onSelectIndex(int index) {
                        switch (index) {
                            case 0:
                                slidePanelEventControlIMPL.changeDataOpening();
                                break;
                            case 1:
                                slidePanelEventControlIMPL.changeDataFront();
                                break;
                            case 2:
                                slidePanelEventControlIMPL.changeDataBlack();
                                break;
                        }
                    }
                });

        ll_change_time_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_change_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sluiceID = (int) tv_stationID.getTag();
                DialogUtil.showEditPositionDialog(MainActivity.this,
                                                  (String) tv_stationLongitude.getTag(),
                                                  (String) tv_stationLatitude.getTag(),realm,
                                                  sluiceID);
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

        ll_change_position = findViewById(R.id.ll_change_position);
        ll_change_time_area = findViewById(R.id.ll_change_time_area);

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
        tv_starttime = (TextView) findViewById(R.id.tv_panel_starttime);
        tv_endtime = (TextView) findViewById(R.id.tv_panel_endtime);

        LinearLayout slideContent = (LinearLayout) findViewById(R.id.slide_panel_content);
        slideContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event) {
                return true;
            }
        });

        fab_close_mini = findViewById(R.id.fab_close_panel_mini);
        realm = Realm.getDefaultInstance();

        scaleAnimation_show = AnimSetUtil.getScaleAnimationFABSHOW();
        scaleAnimation_hidden = AnimSetUtil.getScaleAnimationFABHIDDEN();

        lineChart = (LineChartView) findViewById(R.id.lc_station);
        ec_change_data_type = (ECSegmentedControl) findViewById(R.id.ec_station_change_data);

        slidePanelEventControlIMPL = new SlidePanelEventControlIMPL(this,realm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
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

            try {
                int id = Integer.parseInt(data);

                //通知高德地图转移到相应的监测站
                if (fragmentUtil.getCurrentFragment() instanceof MapFragment) {
                    MapFragment mapFragment = (MapFragment) fragmentUtil.getCurrentFragment();
                    mapFragment.moveMapToStation(id);
                    slidePanelEventControlIMPL.openPanel(id);
                }
            } catch (NumberFormatException e) {
                Logger.d("此 Intent 不来自搜索" + intent);
            }
        }
    }

    public void initStationPosition(MonitoringStationBean element,String stringLo,String stringLa) {
        tv_stationLongitude.setText(String.format(stringLo,element.getLongitude()));
        tv_stationLongitude.setTag(element.getLongitude());
        tv_stationLatitude.setText(String.format(stringLa,element.getLatitude()));
        tv_stationLatitude.setTag(element.getLatitude());
    }

    public void initTextViewIDName(String stringID,MonitoringStationBean theBean) {
        tv_stationID.setText(String.format(stringID,theBean.getSluiceID()));
        tv_stationID.setTag(theBean.getSluiceID());
        tv_stationName.setText(theBean.getName());
    }
}
