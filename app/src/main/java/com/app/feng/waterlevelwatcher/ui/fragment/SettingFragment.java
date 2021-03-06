package com.app.feng.waterlevelwatcher.ui.fragment;


import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.*;
import com.app.feng.waterlevelwatcher.ui.LoginActivity;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.SharedPref;
import com.app.feng.waterlevelwatcher.utils.TimeRangeUtil;
import com.app.feng.waterlevelwatcher.utils.manager.DayNightModeManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.DecimalFormat;

import io.realm.Realm;

public class SettingFragment extends Fragment {

    private View admin_panel;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvAdminName;
    private TextView tvCacheSize;

    private View editTime;
    private View resetTime;

    private SwitchCompat switchDayNight;

    private Realm realm;

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

        realm = Realm.getDefaultInstance();
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
        initAdmin();
    }

    private void initAdmin() {
        String sTemp = getResources().getString(R.string.admin_name);
        String username = RealmUtil.loadUser(realm)
                .getDisplayName();
        tvAdminName.setText(String.format(sTemp,username));

    }

    public void exitSystem() {
        UserBean userBean = RealmUtil.loadUser(realm);
        realm.beginTransaction();
        userBean.deleteFromRealm();
        realm.commitTransaction();
        startActivity(new Intent(getActivity(),LoginActivity.class));
        getActivity().finish();
    }


    private void initEvent() {
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出修改时间对话框 --- 用户修改了默认时间
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("选择时间范围")
                        .setCancelable(true)
                        .setItems(R.array.time_choice_string,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {

                                switch (which) {
                                    case 0:
                                        //三天
                                        TimeRangeUtil.setTimeByRange(getContext(),-3);
                                        break;
                                    case 1:
                                        //五天
                                        TimeRangeUtil.setTimeByRange(getContext(),-5);
                                        break;
                                    case 2:
                                        //十二天
                                        TimeRangeUtil.setTimeByRange(getContext(),-12);
                                        break;
                                    case 3:
                                        //一月
                                        TimeRangeUtil.setTimeByRange(getContext(),-30);
                                        break;
                                    default:

                                }

                                initTime();
                                //通知Panel修改时间
                                EventBus.getDefault()
                                        .post(Config.KEY.CHANGE_DEFAULT_TIME);
                            }
                        })
                        .show();

            }
        });

        resetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置时间
                SharedPref.getInstance(getContext())
                        .putBoolean(Config.KEY.USER_EDIT_DEFAULT_TIME,false);

                TimeRangeUtil.initDefaultTimeRange(getContext().getApplicationContext());

                initTime();
            }
        });

        switchDayNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    DayNightModeManager.setNightMode(getActivity());

                } else {
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
        tvAdminName = (TextView) view.findViewById(R.id.tv_admin_name);
        tvCacheSize = (TextView) view.findViewById(R.id.tv_cache_size);

        view.findViewById(R.id.btn_exit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitSystem();
                    }
                });

        switchDayNight = (SwitchCompat) view.findViewById(R.id.switch_daynight);
        // 根据 SharePref 给switch设置
        boolean isNight = SharedPref.getInstance(getContext().getApplicationContext())
                .getBoolean(Config.KEY.ISNIGHT,false);
        switchDayNight.setChecked(isNight);

        view.findViewById(R.id.rl_cleanCache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(AllLineFlowSpeedBean.class);
                        realm.delete(AllLineSchedulingAnalysisBean.class);
                        realm.delete(AllLineWaterDepthBean.class);
                        realm.delete(FS_StatisticsBean.class);
                        realm.delete(FSKBean.class);
                        realm.delete(SluiceBean.class);
                        realm.delete(UserBean.class);
                        realm.delete(ZDDM_StatisticsBean.class);

                        File realmFile = new File(realm.getPath());
                        tvCacheSize.setText(formatFileSize(realmFile.length()));
                    }
                });

                Toast.makeText(getContext(),"清除成功",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String formatFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (admin_panel != null) {
            if (!hidden) {
                //获取缓存容量
                File realmFile = new File(realm.getPath());
                tvCacheSize.setText(formatFileSize(realmFile.length()));

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}
