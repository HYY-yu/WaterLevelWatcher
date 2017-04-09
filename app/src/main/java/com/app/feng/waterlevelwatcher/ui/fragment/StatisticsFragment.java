package com.app.feng.waterlevelwatcher.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.adapter.StatisticsAdapter;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.ui.MainActivity;
import com.app.feng.waterlevelwatcher.utils.AnimSetUtil;
import com.app.feng.waterlevelwatcher.utils.FragmentUtil;

public class StatisticsFragment extends Fragment {

    ISlidePanelEventControl panelEventControl;

    ScaleAnimation scaleAnimation_show;
    ScaleAnimation scaleAnimation_hidden;

    TabLayout tabLayout;
    private View fab_change_time;
    ViewPager viewPager;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
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
        viewPager.setAdapter(new StatisticsAdapter(getActivity()));
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_title);
        fab_change_time = view.findViewById(R.id.fab_chang_time);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
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
}
