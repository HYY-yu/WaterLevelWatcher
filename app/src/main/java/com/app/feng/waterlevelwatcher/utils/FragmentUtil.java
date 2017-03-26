package com.app.feng.waterlevelwatcher.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.ui.MainActivity;
import com.app.feng.waterlevelwatcher.ui.fragment.MapFragment;
import com.app.feng.waterlevelwatcher.ui.fragment.OverviewFragment;
import com.app.feng.waterlevelwatcher.ui.fragment.SettingFragment;

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


        mainActivityWeakReference = new WeakReference<>(mainActivity);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void addAllFragment() {

        FragmentManager fragmentManager = mainActivityWeakReference.get()
                .getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        overviewFragment = (OverviewFragment) fragmentManager.findFragmentByTag("overview");
        settingFragment = (SettingFragment) fragmentManager.findFragmentByTag("setting");

        if(overviewFragment == null){
            overviewFragment = OverviewFragment.newInstance();
            settingFragment = SettingFragment.newInstance();
            fragmentTransaction.add(R.id.fl_fragment,overviewFragment,"overview");
            fragmentTransaction.add(R.id.fl_fragment,settingFragment,"setting");
        }
        mapFragment = MapFragment.newInstance();

        fragmentTransaction.add(R.id.fl_fragment,mapFragment,"map");
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
