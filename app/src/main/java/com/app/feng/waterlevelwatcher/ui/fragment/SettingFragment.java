package com.app.feng.waterlevelwatcher.ui.fragment;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.app.feng.waterlevelwatcher.utils.TimeRangeUtil;
import com.app.feng.waterlevelwatcher.utils.manager.DayNightModeManager;

public class SettingFragment extends Fragment {

    private View admin_panel;
    private TextView tvStartTime;
    private TextView tvEndTime;

    private View editTime;
    private View resetTime;

    private SwitchCompat switchDayNight;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting,container,false);
    }

    @Override
    public void onViewCreated(
            View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        initView(view);
        initEvent();
        initTime();
    }


    private void initEvent() {
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出修改时间对话框 --- 用户修改了默认时间


            }
        });

        resetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置时间
                SharedPref.getInstance(getContext()).putBoolean(Config.KEY.USER_EDIT_DEFAULT_TIME,false);

                TimeRangeUtil.initDefaultTimeRange(getContext().getApplicationContext());

                initTime();
            }
        });

        switchDayNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    DayNightModeManager.setNightMode(getActivity());

                }else{
                    DayNightModeManager.setDayMode(getActivity());
                }
            }
        });

    }

    private void initTime() {
        String startTime = SharedPref.getInstance(getContext())
                .getString(Config.KEY.DEFAULT_START_TIME,"");

        String endTime = SharedPref.getInstance(getContext())
                .getString(Config.KEY.DEFAULT_END_TIME,"");

        String startTimeString = getString(R.string.start_time_string);
        String endTimeString = getString(R.string.end_time_string);

        tvStartTime.setText(String.format(startTimeString,startTime));
        tvEndTime.setText(String.format(endTimeString,endTime));
    }

    private void initView(View view) {
        admin_panel = view.findViewById(R.id.ll_admin_panel);
        tvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
        editTime = view.findViewById(R.id.btn_edit_time);
        resetTime = view.findViewById(R.id.btn_reset_time);

        switchDayNight = (SwitchCompat) view.findViewById(R.id.switch_daynight);
        // 根据 SharePref 给switch设置
        boolean isNight = SharedPref.getInstance(getContext().getApplicationContext())
                .getBoolean(Config.KEY.ISNIGHT,false);
        switchDayNight.setChecked(isNight);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (admin_panel != null) {
            if (!hidden) {
                ObjectAnimator.ofFloat(admin_panel,"translationY",-admin_panel.getHeight(),0f)
                        .setDuration(400)
                        .start();
            } else {
                ObjectAnimator.ofFloat(admin_panel,"translationY",0f,-admin_panel.getHeight())
                        .setDuration(400)
                        .start();
            }
        }
    }
}
