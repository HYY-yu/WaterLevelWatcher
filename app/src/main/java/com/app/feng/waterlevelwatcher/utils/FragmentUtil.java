package com.app.feng.waterlevelwatcher.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.ui.MainActivity;
import com.app.feng.waterlevelwatcher.ui.fragment.MapFragment;
import com.app.feng.waterlevelwatcher.ui.fragment.OverviewFragment;
import com.app.feng.waterlevelwatcher.ui.fragment.SettingFragment;
import com.app.feng.waterlevelwatcher.ui.fragment.StatisticsFragment;

import java.lang.ref.WeakReference;

/**
 * Created by feng on 2017/3/8.
 */

public class FragmentUtil {

    public MapFragment mapFragment;
    public OverviewFragment overviewFragment;
    public SettingFragment settingFragment;
    public StatisticsFragment statisticsFragment;

    private WeakReference<MainActivity> mainActivityWeakReference;
    private Fragment currentFragment;

    public static boolean IS_SAME_BUTTON = false;

    public FragmentUtil(MainActivity mainActivity) {
        overviewFragment = OverviewFragment.newInstance();
        settingFragment = SettingFragment.newInstance();
        statisticsFragment = StatisticsFragment.newInstance();
        mapFragment = MapFragment.newInstance();

        mainActivityWeakReference = new WeakReference<>(mainActivity);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void addAllFragment() {
        FragmentManager fragmentManager = mainActivityWeakReference.get()
                .getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fl_fragment,overviewFragment,Config.Constant.OVERVIEW);
        fragmentTransaction.add(R.id.fl_fragment,settingFragment,Config.Constant.SETTING);
        fragmentTransaction.add(R.id.fl_fragment,statisticsFragment,Config.Constant.STATISTICS);
        fragmentTransaction.add(R.id.fl_fragment,mapFragment,Config.Constant.MAP);
        fragmentTransaction.commitNow();
    }

    public void setMapFragment() {
        IS_SAME_BUTTON = false;

        FragmentTransaction fragmentTransaction = mainActivityWeakReference.get()
                .getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);

        fragmentTransaction.show(mapFragment);
        fragmentTransaction.hide(overviewFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.hide(statisticsFragment);
        fragmentTransaction.commitNow();

        currentFragment = mapFragment;
    }

    public void setOverviewFragment() {
        IS_SAME_BUTTON = true;

        FragmentTransaction fragmentTransaction = mainActivityWeakReference.get()
                .getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
        //        View shareView = overviewFragment.getView()
        //                .findViewById(R.id.fab_chang_time);
        //        if(shareView != null){
        //            fragmentTransaction.addSharedElement(shareView,"fab_change_time");
        //        }

        fragmentTransaction.show(overviewFragment);
        fragmentTransaction.hide(mapFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.hide(statisticsFragment);
        //        fragmentTransaction.replace(R.id.fl_fragment,overviewFragment);
        fragmentTransaction.commitNow();


        currentFragment = overviewFragment;
    }

    public void setSettingFragment() {
        IS_SAME_BUTTON = false;

        FragmentTransaction fragmentTransaction = mainActivityWeakReference.get()
                .getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);

        fragmentTransaction.show(settingFragment);
        fragmentTransaction.hide(mapFragment);
        fragmentTransaction.hide(overviewFragment);
        fragmentTransaction.hide(statisticsFragment);
        fragmentTransaction.commitNow();

        currentFragment = settingFragment;
    }

    public void setStatisticsFragment() {
        IS_SAME_BUTTON = true;

        FragmentTransaction fragmentTransaction = mainActivityWeakReference.get()
                .getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);

        fragmentTransaction.show(statisticsFragment);
        fragmentTransaction.hide(mapFragment);
        fragmentTransaction.hide(overviewFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.commitNow();

        currentFragment = statisticsFragment;
    }

}
