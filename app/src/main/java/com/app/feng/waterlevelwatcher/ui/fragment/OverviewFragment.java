package com.app.feng.waterlevelwatcher.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.adapter.OverviewAdapter;
import com.app.feng.waterlevelwatcher.interfaces.ISlidePanelEventControl;
import com.app.feng.waterlevelwatcher.ui.MainActivity;

/**
 * 进入后,判断数据库中是否有默认时间点的数据,没有就到服务器上去取,并保存或者更新到本地数据库.
 * 选择一个时间点,判断本地数据库中是否有该时间点的数据,有则显示,没有就去服务器上加载.
 * <p>
 * Created by feng on 2017/3/8.
 */

public class OverviewFragment extends BaseLoadingFragment {

    private ISlidePanelEventControl panelControl;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static OverviewFragment newInstance() {
        OverviewFragment overviewFragment = new OverviewFragment();
        return overviewFragment;
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        setRetainInstance(true);

        View v = inflater.inflate(R.layout.fragment_overview,container,false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event) {
                return true;
            }
        });

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout_title);

        viewPager.setAdapter(new OverviewAdapter(getContext(),getChildFragmentManager(),panelControl));

        tabLayout.setupWithViewPager(viewPager);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            panelControl = ((MainActivity) context).slidePanelEventControlIMPL;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement ISlidePanelEventControl");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        panelControl = null;
    }
}
