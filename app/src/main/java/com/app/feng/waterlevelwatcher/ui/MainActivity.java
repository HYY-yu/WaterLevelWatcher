package com.app.feng.waterlevelwatcher.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.SluiceBean;
import com.app.feng.waterlevelwatcher.log.XLog;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.app.feng.waterlevelwatcher.utils.Utils;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MapFragment.OnMapFragmentInteractionListener {

    BottomNavigationBar bottomNavigationBar;
    SlidingUpPanelLayout slidingUpPanelLayout;

    FrameLayout fragmentContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initFragment();

        initEvent();

        loadJSON();

    }

    //第一次进入时加载JSON到数据库
    private void loadJSON() {
        if (checkDataExist()) {
            //存在
            return;
        } else {
            List<SluiceBean> temp = Utils.fromJson(getBaseContext());
            Utils.saveToRealm(temp);

            SharedPref.getInstance(getBaseContext())
                    .putBoolean(Config.KEY.JSON_LOAD,true);
        }
    }

    private boolean checkDataExist() {
        return SharedPref.getInstance(getBaseContext())
                .contains(Config.KEY.JSON_LOAD);
    }

    private void initEvent() {
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                FragmentTransaction fragmentTransation = getSupportFragmentManager().beginTransaction();

                //FIXME : 此处是否不需要判断Fragment ？
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_fragment);
                XLog.d("fragment : " + fragment.toString(),null);

                switch (position) {
                    case 0:
                        if (fragment instanceof MapFragment) {
                            break;
                        }else{
                            fragmentTransation.replace(R.id.fl_fragment,MapFragment.newInstance());
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:

                }
                fragmentTransation.commit();
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MapFragment mapFragment = MapFragment.newInstance();
        fragmentTransaction.add(R.id.fl_fragment,mapFragment,"map");
        fragmentTransaction.commit();
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

        fragmentContent = (FrameLayout) findViewById(R.id.fl_fragment);
    }

    @Override
    public void onMapFragmentClickMap(Uri uri) {

    }
}
