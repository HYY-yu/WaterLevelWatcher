package com.app.feng.waterlevelwatcher.ui;

import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.app.feng.waterlevelwatcher.R;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    BottomNavigationBar bottomNavigationBar;
    SlidingUpPanelLayout slidingUpPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏 加 状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initView();

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

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.spl_main_content);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

    }
}
