package com.app.feng.waterlevelwatcher.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.adapter.StatisticsAdapter;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.ui.MainActivity;
import com.app.feng.waterlevelwatcher.utils.*;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.Date;

import io.realm.Realm;

public class StatisticsFragment extends BaseLoadingFragment {

    ISlidePanelEventControl panelEventControl;

    ScaleAnimation scaleAnimation_show;
    ScaleAnimation scaleAnimation_hidden;

    TabLayout tabLayout;
    private View fab_change_time;
    ViewPager viewPager;


    Realm realm;
    private boolean changeTimeSelector;

    public StatisticsFragment() {
        // Required empty public constructor
        scaleAnimation_show = AnimSetUtil.getScaleAnimationFABSHOW();
        scaleAnimation_hidden = AnimSetUtil.getScaleAnimationFABHIDDEN();
    }

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        realm = Realm.getDefaultInstance();

        return inflater.inflate(R.layout.fragment_statistics,container,false);
    }

    @Override
    public void onViewCreated(
            View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        initView(view);

        initTabLayout();

        initViewPager();
    }

    private void initViewPager() {
        viewPager.setAdapter(new StatisticsAdapter(this,realm));
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_title);
        fab_change_time = view.findViewById(R.id.fab_chang_time);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        final String dEndTime = SharedPref.getInstance(getContext())
                .getString(Config.KEY.DEFAULT_END_TIME,Utils.format(new Date()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Select pos 3\4  即分水统计和重点断面,注意: 此时TimeSelector时间格式改为 :yyyyMMdd
                if (position == 3 || position == 4) {
                    changeTimeSelector = true;
                } else {
                    changeTimeSelector = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab_change_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeTimeSelector) {
                    TimeRangeUtil.showTimeSelectorByYYYYMMDD(getContext(),Config.Constant.GLOBAL_START_TIME,
                                                             dEndTime,new TimeSelector.ResultHandler() {
                                @Override
                                public void handle(String s) {

                                    int cItem = viewPager.getCurrentItem();
                                    ((StatisticsAdapter) viewPager.getAdapter()).notifyCurrentPageTimeChange(
                                            cItem,s);
                                }
                            });
                } else {
                    TimeRangeUtil.showTimeSelector(getContext(),Config.Constant.GLOBAL_START_TIME,
                                                   dEndTime,new TimeSelector.ResultHandler() {
                                @Override
                                public void handle(String s) {
                                    int cItem = viewPager.getCurrentItem();
                                    ((StatisticsAdapter) viewPager.getAdapter()).notifyCurrentPageTimeChange(
                                            cItem,s);
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void reloadingData() {
        ((StatisticsAdapter) viewPager.getAdapter()).notifyReloadData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            panelEventControl = ((MainActivity) context).slidePanelEventControlIMPL;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement ISlidePanelEventControl");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        panelEventControl = null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (fab_change_time != null && !FragmentUtil.IS_SAME_BUTTON) {
            if (!hidden) {
                fab_change_time.startAnimation(scaleAnimation_show);
            } else {
                fab_change_time.startAnimation(scaleAnimation_hidden);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}
