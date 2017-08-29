package com.app.feng.waterlevelwatcher.adapter;

import android.widget.TextView;

import com.app.feng.waterlevelwatcher.bean.ZDDM_StatisticsBean;

import java.util.List;

/**
 * Created by feng on 2017/4/27.
 */

public class ZDDMStatisticsFixTableAdapter extends FixTableAdapter {
    private List<ZDDM_StatisticsBean> data;

    public ZDDMStatisticsFixTableAdapter(String[] titles,List<ZDDM_StatisticsBean> data) {
        super(titles);
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void convertData(int position,List<TextView> bindViews) {
        super.convertData(position,bindViews);

        ZDDM_StatisticsBean bean = data.get(position);

        bindViews.get(0).setText(bean.jctName);
        bindViews.get(1).setText(String.valueOf(bean.getFlow()));
        bindViews.get(2).setText(String.valueOf(bean.getCurflow()));
        bindViews.get(3).setText(String.valueOf(bean.getTotalflow()));

    }

    @Override
    public void convertLeftData(int position,TextView textView) {
        super.convertLeftData(position,textView);

        ZDDM_StatisticsBean bean = data.get(position);

        textView.setText(bean.jctName);
    }
}
