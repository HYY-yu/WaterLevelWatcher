package com.app.feng.waterlevelwatcher.adapter;

import android.widget.TextView;

import com.app.feng.waterlevelwatcher.bean.AllLineFlowSpeedBean;

import java.util.List;

/**
 * Created by feng on 2017/4/26.
 */

public class AllLineFlowSpeedFixTableAdapter extends FixTableAdapter {

    private List<AllLineFlowSpeedBean> data;

    public AllLineFlowSpeedFixTableAdapter(String[] titles,List<AllLineFlowSpeedBean> data) {
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
        AllLineFlowSpeedBean bean = data.get(position);


        //jctname varchar(100),		--节制闸名称
        //location varchar(100),		--安装位置
        //timepoint1 float,			--时间点1
        //timepoint2 float,			--时间点2
        //timepoint3 float,			--时间点3
        //timepoint4 float,			--时间点4
        //timepoint5 float,			--时间点5
        //timepoint6 float,			--时间点6
        //timepoint7 float,			--时间点7
        //timepoint8 float,			--时间点8
        //timepoint9 float,			--时间点9
        //timepoint10 float,		--时间点10
        //timepoint11 float,		--时间点11
        //timepoint12 float			--时间点12

        bindViews.get(0).setText(bean.getJctname());
        bindViews.get(1).setText(String.valueOf(bean.getLocation()));
        bindViews.get(2).setText(String.valueOf(bean.getTimepoint1()));
        bindViews.get(3).setText(String.valueOf(bean.getTimepoint2()));
        bindViews.get(4).setText(String.valueOf(bean.getTimepoint3()));
        bindViews.get(5).setText(String.valueOf(bean.getTimepoint4()));
        bindViews.get(6).setText(String.valueOf(bean.getTimepoint5()));
        bindViews.get(7).setText(String.valueOf(bean.getTimepoint6()));
        bindViews.get(8).setText(String.valueOf(bean.getTimepoint7()));
        bindViews.get(9).setText(String.valueOf(bean.getTimepoint8()));
        bindViews.get(10).setText(String.valueOf(bean.getTimepoint9()));
        bindViews.get(11).setText(String.valueOf(bean.getTimepoint10()));
        bindViews.get(12).setText(String.valueOf(bean.getTimepoint11()));
        bindViews.get(13).setText(String.valueOf(bean.getTimepoint12()));
    }

    @Override
    public void convertLeftData(int position,TextView textView) {
        super.convertLeftData(position,textView);

        AllLineFlowSpeedBean dataBean = data.get(position);

        textView.setText(dataBean.getJctname());
    }
}
