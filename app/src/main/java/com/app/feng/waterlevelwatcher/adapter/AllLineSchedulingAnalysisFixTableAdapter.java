package com.app.feng.waterlevelwatcher.adapter;

import android.content.Intent;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.bean.AllLineSchedulingAnalysisBean;
import com.app.feng.waterlevelwatcher.ui.Hour24JzzActivity;
import com.app.feng.waterlevelwatcher.utils.Utils;

import java.util.Date;
import java.util.List;

/**
 * Created by feng on 2017/4/25.
 */

public class AllLineSchedulingAnalysisFixTableAdapter extends FixTableAdapter {
    public List<AllLineSchedulingAnalysisBean> data;

    public AllLineSchedulingAnalysisFixTableAdapter(
            String[] title,List<AllLineSchedulingAnalysisBean> data) {
        super(title);
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void convertData(int position,List<TextView> bindViews) {
        super.convertData(position,bindViews);
        AllLineSchedulingAnalysisBean bean = data.get(position);
        //        jctname varchar(100),		--节制闸名称
        //        objectstage float,			--目标水位(m)
        //        designstage float,			--设计水位(m)
        //        currentstage float,			--当前水位(m)
        //        toobjectstage float,		--距目标水位(m)
        //        todesignstage float,		--距设计水位(m)
        //        rose2 float,				--2h涨幅(m)
        //        rose24 float,				--24h涨幅(m)
        //        trend24 float				--24h涨幅趋势(m)*/

        bindViews.get(0)
                .setText(bean.getJctname());
        bindViews.get(1)
                .setText(String.valueOf(bean.getObjectstage()));
        bindViews.get(2)
                .setText(String.valueOf(bean.getDesignstage()));
        bindViews.get(3)
                .setText(String.valueOf(bean.getCurrentstage()));
        bindViews.get(4)
                .setText(String.valueOf(bean.getToobjectstage()));
        bindViews.get(5)
                .setText(String.valueOf(bean.getTodesignstage()));
        bindViews.get(6)
                .setText(String.valueOf(bean.getRose2()));
        bindViews.get(7)
                .setText(String.valueOf(bean.getRose24()));

        //趋势计算   ------
        TextView textView = bindViews.get(8);

        if (bean.getTrend24() != null) {
            float trend24 = bean.getTrend24();

            if (trend24 == 0) {
                textView.setText("未变化");
            } else if (trend24 > 0) {
                // 上升
                VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(
                        textView.getResources(),R.drawable.ic_arrow_upward_red_24dp,null);
                int size = textView.getResources()
                        .getDimensionPixelOffset(R.dimen.allline_table_arrow_size);
                vectorDrawableCompat.setBounds(0,0,size,size);

                SpannableString spannableString = new SpannableString(" ");
                ImageSpan imageSpan = new ImageSpan(vectorDrawableCompat);
                spannableString.setSpan(imageSpan,0,1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                textView.setText(spannableString);

            } else {
                // 下降
                VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(
                        textView.getResources(),R.drawable.ic_arrow_downward_green_24dp,null);
                int size = textView.getResources()
                        .getDimensionPixelOffset(R.dimen.allline_table_arrow_size);
                vectorDrawableCompat.setBounds(0,0,size,size);

                SpannableString spannableString = new SpannableString(" ");
                ImageSpan imageSpan = new ImageSpan(vectorDrawableCompat);
                spannableString.setSpan(imageSpan,0,1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                textView.setText(spannableString);
            }

            textView.setTag(bean);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //根据  节制闸id Time 查询 24小时闸前水位趋势
                    AllLineSchedulingAnalysisBean bean = (AllLineSchedulingAnalysisBean) v.getTag();
                    int id = bean.getJctid();
                    Date time = bean.getQueryDate();

                    // 开启Activity
                    Intent intent = new Intent(v.getContext(),Hour24JzzActivity.class);
                    intent.putExtra(Config.KEY.HOUR24ID,id);
                    intent.putExtra(Config.KEY.HOUR24TIME,Utils.format(time));

                    v.getContext()
                            .startActivity(intent);
                }
            });
        } else {
            textView.setText(String.valueOf(bean.getTrend24()));
        }
    }


    @Override
    public void convertLeftData(int position,TextView textView) {
        super.convertLeftData(position,textView);

        AllLineSchedulingAnalysisBean dataBean = data.get(position);
        textView.setText(dataBean.getJctname());
    }
}
