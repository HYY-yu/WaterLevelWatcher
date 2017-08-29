package com.app.feng.waterlevelwatcher.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.AllLineWaterDepthBean;

import java.util.List;

/**
 * Created by feng on 2017/4/26.
 */

public class AllLineWaterDepthFixTableAdapter extends FixTableAdapter {

    private List<AllLineWaterDepthBean> data;


    public AllLineWaterDepthFixTableAdapter(String[] titles,List<AllLineWaterDepthBean> data) {
        super(titles);
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void convertData(int i,List<TextView> bindViews) {
        AllLineWaterDepthBean bean = data.get(i);

        for (TextView bindView : bindViews) {
            bindView.setLines(1);
            int itemWidth = bindView.getContext()
                    .getResources()
                    .getDimensionPixelOffset(R.dimen.table_item_width);
            bindView.setMaxWidth(itemWidth);
            bindView.setEllipsize(TextUtils.TruncateAt.END);
        }
        //jctid numeric(18, 0),
        //jctname varchar(100),		--节制闸名称
        //fronttopelev float,			--闸前渠底高程(m)
        //backtopelev float,			--闸后渠底高程(m)
        //designstage float,			--设计水位(m)
        //frontwl float,				--闸前水位(m)
        //backwl float,				--闸后水位(m)
        //frontchannelwl float,		--闸前渠道水深(m)
        //backchannelwl float,		--闸后渠道水深(m)
        //todesignstage float			--当前水位与设计水位差(m)

        bindViews.get(0).setText(bean.getJctname());
        bindViews.get(1).setText(String.valueOf(bean.getFronttopelev()));
        bindViews.get(2).setText(String.valueOf(bean.getBacktopelev()));
        bindViews.get(3).setText(String.valueOf(bean.getDesignstage()));
        bindViews.get(4).setText(String.valueOf(bean.getFrontwl()));
        bindViews.get(5).setText(String.valueOf(bean.getBackwl()));
        bindViews.get(6).setText(String.valueOf(bean.getFrontchannelwl()));
        bindViews.get(7).setText(String.valueOf(bean.getBackchannelwl()));
        bindViews.get(8).setText(String.valueOf(bean.getTodesignstage()));
    }

    @Override
    public void convertLeftData(int i,TextView textView) {
        AllLineWaterDepthBean dataBean = data.get(i);
        textView.setLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(dataBean.getJctname());
    }
}

