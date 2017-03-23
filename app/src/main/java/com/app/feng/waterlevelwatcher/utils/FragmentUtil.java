package com.app.feng.waterlevelwatcher.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.ui.MainActivity;
import com.app.feng.waterlevelwatcher.ui.MapFragment;
import com.app.feng.waterlevelwatcher.ui.OverviewFragment;
import com.app.feng.waterlevelwatcher.ui.SettingFragment;

import java.lang.ref.WeakReference;

/**
 * Created by feng on 2017/3/8.
 */

public class FragmentUtil {

    public MapFragment mapFragment;
    public OverviewFragment overviewFragment;
    public SettingFragment settingFragment;
    WeakReference<MainActivity> mainActivityWeakReference;

    Fragment currentFragment;

    public FragmentUtil(MainActivity mainActivity) {
        mapFragment = MapFragment.newInstance();
        overviewFragment = OverviewFragment.newInstance();
        settingFragment = SettingFragment.newInstance();

        mainActivityWeakReference = new WeakReference<>(mainActivity);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void addAllFragment() {
        FragmentTransaction fragmentTransaction = mainActivityWeakReference.get()
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.add(R.id.fl_fragment,mapFragment,"map");
        fragmentTransaction.add(R.id.fl_fragment,overviewFragment,"overview");
        fragmentTransaction.add(R.id.fl_fragment,settingFragment,"setting");
        fragmentTransaction.commitNow();
    }

    public void setMapFragment() {
        FragmentTransaction fragmentTransaction = mainActivityWeakReference.get()
                .getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);

        fragmentTransaction.show(mapFragment);
        fragmentTransaction.hide(overviewFragment);
        fragmentTransaction.hide(settingFragment);
        //        fragmentTransaction.replace(R.id.fl_fragment,mapFragment);
        fragmentTransaction.commitNow();

        currentFragment = mapFragment;
    }

    public void setOverviewFragment() {
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
        //        fragmentTransaction.replace(R.id.fl_fragment,overviewFragment);
        fragmentTransaction.commitNow();

        currentFragment = overviewFragment;
    }

    public void setSettingFragment() {
        FragmentTransaction fragmentTransaction = mainActivityWeakReference.get()
                .getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
        //        View shareView = overviewFragment.getView()
        //                .findViewById(R.id.fab_chang_time);
        //        if(shareView != null){
        //            fragmentTransaction.addSharedElement(shareView,"fab_change_time");
        //        }

        fragmentTransaction.show(settingFragment);
        fragmentTransaction.hide(mapFragment);
        fragmentTransaction.hide(overviewFragment);
        //        fragmentTransaction.replace(R.id.fl_fragment,overviewFragment);
        fragmentTransaction.commitNow();

        currentFragment = settingFragment;
    }

}
